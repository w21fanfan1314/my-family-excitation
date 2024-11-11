package family.excitation.service.api

import family.excitation.service.train.Train

class TrainApiController {

    def list() {
        respond new ApiResult(code: 200, msg: '查询成功', data: Train.findAll())
    }
}
