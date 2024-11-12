package family.excitation.service.api

import family.excitation.service.train.Question
import family.excitation.service.train.Train
import family.excitation.service.train.TrainLevel

class TrainApiController {

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
        respond new ApiResult(code: 200, msg: '查询成功', data: [levels: result, total: TrainLevel.countByTrain(train)])
    }

    def questions(TrainLevel level) {
        if (!level) {
            respond new ApiResult(code: 400, msg: '参数错误')
            return
        }
        def result = Question.createCriteria().list {
            eq('level', level)
            order('dateCreated', 'desc')
        }
        respond new ApiResult(code: 200, msg: '查询成功', data: result)
    }
}
