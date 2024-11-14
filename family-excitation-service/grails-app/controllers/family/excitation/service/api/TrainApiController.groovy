package family.excitation.service.api

import family.excitation.service.Currency
import family.excitation.service.Login
import family.excitation.service.MediaData
import family.excitation.service.train.Question
import family.excitation.service.train.QuestionOption
import family.excitation.service.train.QuestionService
import family.excitation.service.train.QuestionType
import family.excitation.service.train.Train
import family.excitation.service.train.TrainLevel
import family.excitation.service.train.TrainLevelAwardType
import family.excitation.service.train.TrainLevelService
import family.excitation.service.train.TrainService
import family.excitation.service.train.Transcript
import family.excitation.service.train.TranscriptService
import family.excitation.service.train.UserAnswer
import family.excitation.service.train.UserAnswerService
import grails.converters.JSON
import grails.util.Environment
import groovy.json.JsonSlurper
import org.grails.web.json.JSONArray

class TrainApiController {

    static allowedMethods = [commit: 'POST']

    UserAnswerService userAnswerService
    TranscriptService transcriptService
    TrainService trainService
    TrainLevelService trainLevelService
    QuestionService questionService

    def generateAll() {
        def rootPath
        switch (Environment.current) {
            case Environment.DEVELOPMENT:
                rootPath = 'D:\\Downloads\\trains';
                break
            case Environment.PRODUCTION:
                rootPath = '/www/wwwroot/47.120.23.110/trains'
                break
        }
        def rootFile = new File(rootPath)
        def jsonSlurper = new JsonSlurper()
        rootFile.eachFile {
            if (it.directory) {
                Train train = Train.findByName(it.name)
                if (!train) {
                    train = trainService.save(new Train(name: it.name))
                }
                it.eachFile {File file ->
                   if (file.file && file.name.endsWith('.json')) {
                       def name = file.name.substring(0, file.name.lastIndexOf(".json"))
                       def commands = name.split('-')
                       if (commands.length == 5) {
                           def levelValue = commands[0]
                           def awardValue = commands[1]
                           def maxCountValue = commands[2]
                           def currencyValue = commands[3]
                           def awardTypeValue = commands[4]
                           if (levelValue.integer && awardValue.double && maxCountValue.integer) {
                               def level = levelValue.toInteger()
                               TrainLevel trainLevel = TrainLevel.findByTrainAndLevel(train, level)
                               if (!trainLevel) {
                                   trainLevel = trainLevelService.save(new TrainLevel(train: train, level: level, award: awardValue.toDouble(),
                                           awardMaxCount: maxCountValue.toInteger(),
                                           currency: Currency.findByName(currencyValue),
                                           awardType: TrainLevelAwardType.valueOf(awardTypeValue)))
                               }
                               def json = jsonSlurper.parse(file)
                               json.each {
                                   if (it.content) {
                                       def question = Question.findByLevelAndContent(trainLevel, it.content)
                                       if (!question) {
                                           def mediaDataList = it.media?.split(',')
                                           questionService.save(new Question(content: it.content, type: QuestionType.valueOf(it.type), level: trainLevel,
                                                   options: it.options?.collect {String opt -> new QuestionOption(option: opt, isRight: opt == it.answer)},
                                                   meidaDataList: mediaDataList?.collect {String url -> new MediaData(url: url)}
                                           ))
                                       }
                                   }
                               }
                           }
                       }
                   }
                }
            }
        }
        respond new ApiResult(code: 200, msg: '生成成功')
    }

    def categories() {
        def result = Train.createCriteria().list {
            projections {
                groupProperty('category')
            }
        }
        respond new ApiResult(code: 200, msg: '查询成功', data: result)
    }

    def list(int page, int size) {
        if (size == 0) {
            size = 20
        }
        String category = params.category
        def result = Train.createCriteria().list {
            if (category) {
                eq('category', category)
            }
            order('dateCreated', 'desc')
            maxResults(size)
            firstResult(page * size)
        }

        def rowCount = Train.createCriteria().get {
            if (category) {
                eq('category', category)
            }
            projections {
                rowCount()
            }
        }
        respond new ApiResult(code: 200, msg: '查询成功', data: [trains: result, total: rowCount])
    }

    def levels(Train train, int page, int size) {
        if (!train) {
            respond new ApiResult(code: 400, msg: '参数错误')
            return
        }
        def result = TrainLevel.createCriteria().list {
            eq('train', train)
            order('level', 'asc')
            maxResults(size)
            firstResult(page * size)
        }

        def user = request.getAttribute("user_login")?.user
        def data = result?.collect { TrainLevel level ->
            return [
                    id: level.id,
                    level: level.level,
                    questionCount: level.questionCount,
                    award: level.award,
                    awardMaxCount: level.awardMaxCount,
                    starCount: level.bestTranscriptByUser(user)?.starCount,
                    questions: level.questions,
                    dateCreated: level.dateCreated,
                    lastUpdated: level.lastUpdated
            ]
        }
        respond new ApiResult(code: 200, msg: '查询成功', data: [levels: data, total: TrainLevel.countByTrain(train)])
    }

    def questions(TrainLevel level) {
        if (!level) {
            respond new ApiResult(code: 400, msg: '参数错误')
            return
        }
        def result = Question.createCriteria().list {
            eq('level', level)
        }
        respond new ApiResult(code: 200, msg: '查询成功', data: result)
    }

    def commit() {
        def token = request.getHeader('app-token')
        if (!token) {
            respond new ApiResult(code: 401, msg: '未登录')
            return
        }
        def user = Login.findByToken(token)?.user
        if (!user) {
            respond new ApiResult(code: 401, msg: '未登录')
            return
        }

        def json = request.JSON
        if (!json) {
            respond new ApiResult(code: 400, msg: '参数错误')
            return
        }
        def levelId = json['level.id']
        if (!levelId) {
            respond new ApiResult(code: 400, msg: '无法获取关卡ID')
            return
        }
        def level = TrainLevel.get(levelId)
        if (!level) {
            respond new ApiResult(code: 400, msg: '关卡不存在')
            return
        }
        def commitData = json.commitData
        if (!commitData) {
            respond new ApiResult(code: 400, msg: '答案不能为空')
            return
        }
        def transcript = new Transcript(user: user, level: level)
        level.questions.each { Question question ->
            def answer = null
            for (def item in commitData) {
                def qid = item.id
                if (qid == question.id) {
                    answer = item.answer
                }
            }
            def userAnswer = null
            switch (question.type) {
                case QuestionType.SINGLE:
                case QuestionType.JUDGE:
                case QuestionType.ANSWER:
                    userAnswer = answer
                    break
                case QuestionType.MULTIPLE:
                    userAnswer = answer?.join(',')
                    break
            }
            transcript.addToAnswers(new UserAnswer(question: question, user: user, answer: userAnswer))
        }
        def result = transcriptService.save(transcript)
        respond new ApiResult(code: 200, msg: '提交成功', data: result)
    }

    def transcriptDetail(Transcript transcript) {
        respond new ApiResult(code: 200, msg: '查询成功', data: transcript)
    }
}
