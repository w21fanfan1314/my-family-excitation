package family.excitation.service.api

import family.excitation.service.Login
import family.excitation.service.LoginService
import family.excitation.service.LoginType
import family.excitation.service.User
import grails.converters.JSON

class UserApiController {
    static allowedMethods = [login: ['GET', 'POST']]

    LoginService loginService

    def index() { }

    def login() {
        if (request.isPost()) {
            def loginName = params.loginName
            def password = params.password
            def type = LoginType.valueOf(params.type ?: 'PC')
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
                    JSON.use('deep') {
                        respond new ApiResult(code: 200, msg: '登录成功', data: token), formats: ['json']
                    }
                }
            }
        }
    }
}
