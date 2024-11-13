package family.excitation.service.api

import family.excitation.service.Login
import family.excitation.service.train.Question
import family.excitation.service.train.QuestionType
import family.excitation.service.train.Train
import family.excitation.service.train.TrainLevel
import family.excitation.service.train.Transcript
import family.excitation.service.train.TranscriptService
import family.excitation.service.train.UserAnswer
import family.excitation.service.train.UserAnswerService
import grails.converters.JSON

class TrainApiController {

    static allowedMethods = [commit: 'POST']

    UserAnswerService userAnswerService
    TranscriptService transcriptService

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
