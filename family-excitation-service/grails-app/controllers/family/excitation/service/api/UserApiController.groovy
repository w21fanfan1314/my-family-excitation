package family.excitation.service.api

import family.excitation.service.Login
import family.excitation.service.Currency
import family.excitation.service.MediaData
import family.excitation.service.MediaDataService
import family.excitation.service.Score
import family.excitation.service.UserGender
import family.excitation.service.UserRecord
import family.excitation.service.LoginService
import family.excitation.service.LoginType
import family.excitation.service.User
import family.excitation.service.UserService
import grails.converters.JSON

import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoField

class UserApiController {
    static allowedMethods = [login: ['GET', 'POST'], update: 'POST', updatePwd: 'POST']

    LoginService loginService
    UserService userService
    MediaDataService mediaDataService

    def show(User user) {
        respond new ApiResult(code: 200, msg: '查询成功', data: user)
    }

    def login() {
        if (request.isPost()) {
            def loginName
            def password 
            def type

            request.withFormat {
                json {
                    def json = request.JSON
                    loginName = json.loginName
                    password = json.password
                    type = LoginType.valueOf(json.type ?: 'WEB')
                }
                form {
                    loginName = params.loginName
                    password = params.password
                    type = LoginType.valueOf(params.type ?: 'PC')
                }
            }
            def user = User.createCriteria().get {
                or {
                    eq('userName', loginName)
                    eq('name', loginName)
                }
                eq('password', password)
            }
            if (!user) {
                withFormat {
                    html {
                        render view: 'login', model: [error: '用户名或密码错误', loginName: loginName, password: password]
                    }
                    json {
                        respond new ApiResult(code: 401, msg: '用户名或密码错误'), formats: ['json']
                    }
                }
                return
            }
            def token = Login.findByUserAndType(user, type)
            if (!token) {
                token = new Login(user: user, type: type, token: Login.generateToken())
            } else {
                token.token = Login.generateToken()
            }
            loginService.save(token)
            withFormat {
                html {
                    session.login = token
                    redirect(uri: '/')
                }
                json {
                    respond new ApiResult(code: 200, msg: '登录成功', data: token)
                }
            }
        }
    }

    def updatePwd() {
        def json = request.JSON
        if (!json.userId) {
            respond new ApiResult(code: 400, msg: '用户不存在')
            return
        }
        def user = User.findByIdAndPassword(json.userId, json.password)
        if (!user) {
            respond new ApiResult(code: 400, msg: '原始密码不正确')
            return
        }

        if(!json.newPassword) {
            respond new ApiResult(code: 400, msg: '新密码不能为空')
            return
        }

        if(json.newPassword.length() < 6) {
            respond new ApiResult(code: 400, msg: '新密码必须大于等于6为')
            return
        }


        user.password = json.newPassword
        def result = userService.save(user)
        respond new ApiResult(code: 200, msg: '修改成功', data: result)
    }

    // 更新用户信息
    def update() {
        def json = request.JSON
        if (!json.userId) {
            respond new ApiResult(code: 400, msg: '用户不存在')
            return
        }
        def user = User.get(json.userId)
        if (!user) {
            respond new ApiResult(code: 400, msg: '用户不存在')
            return
        }
        if (json.name && !json.name.blank) {
            user.name = json.name
        }
        if (json.gender && !json.gender.blank) {
            user.gender = UserGender.valueOf(json.gender)
        }
        user.birthday = new Date(json.birthday)

        def avatar = MediaData.findByUrlAndUser(user.avatar, user)
        if (avatar) {
            mediaDataService.delete(avatar.id)
        }

        if (json.avatar) {
            avatar = MediaData.findByUrl(json.avatar)
            if (avatar) {
                avatar.user = user
                mediaDataService.save(avatar)
            }
        }
        user.avatar = json.avatar
        def result = userService.save(user)
        respond new ApiResult(code: 200, msg: '更新成功', data: result)
    }

    def quit() {
        def token = request.getHeader('app-token')
        if (token) {
            Login.findByToken(token)?.delete()
        }
        withFormat {
            html {
                redirect(action: 'login')
            }
            json {
                respond new ApiResult(code: 200, msg: '退出成功'), formats: ['json']
            }
        }
    }

    def checkAuthValid() {
        def token = request.getHeader('app-token')
        if (!token) {
            respond new ApiResult(code: 401, msg: '未登录')
        } else {
            def login = Login.findByToken(token)
            if (login) {
                respond new ApiResult(code: 200, msg: '已登录', data: login)
            } else {
                respond new ApiResult(code: 401, msg: '未登录')
            }
        }
    }

    def queryBalance(User user) {
        def balance = Currency.list().collect {
            def records = UserRecord.findAllByUserAndCurrency(user, it)
            [currency: it, balance: records ? records.collect { it.amount }.sum() : 0]
        }
        respond new ApiResult(code: 200, msg: '查询成功', data: balance)
    }

    def queryScore(User user, long time) {
        def timeRange = timeToRange(time)
        def scores = Score.createCriteria().list {
            eq('user', user)
            between('dateCreated', timeRange[0], timeRange[1])
        }
        def starCount = scores.findAll { it.score > 85 }.size()
        respond new ApiResult(code: 200, msg: '查询成功', data: [scores: scores, starCount: starCount])
    }

    def queryTopAScore(long time) {
        def timeRange = timeToRange(time)
        def scores = Score.createCriteria().list {
            ge('score', 85)
            between('dateCreated', timeRange[0], timeRange[1])
        }
        respond new ApiResult(code: 200, msg: '查询成功', data: scores)
    }

    def userRecords(User user, Integer page, Integer size) {
        def records = UserRecord.createCriteria().list {
            eq('user', user)
            order('dateCreated', 'desc')
            maxResults(size)
            firstResult(page * size)
        }
        respond new ApiResult(code: 200, msg: '查询成功', data: [records: records, total: UserRecord.countByUser(user)])
    }

    private static def timeToRange(long time) {
        def begin = new Date(Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).with(ChronoField.NANO_OF_DAY, 0).toInstant().toEpochMilli())
        def end = new Date(begin.time + 24 * 60 * 60 * 1000 - 1000)
        return [begin, end]
    }
}
