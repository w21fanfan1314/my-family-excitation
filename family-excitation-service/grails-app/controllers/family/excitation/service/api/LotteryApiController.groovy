package family.excitation.service.api

import family.excitation.service.Lottery
import family.excitation.service.LotteryRecords
import family.excitation.service.LotteryRecordsService
import family.excitation.service.User

class LotteryApiController {
    static allowedMethods = [saveLotteryRecords: "POST", queryLotteryRecords: "GET", giveLotteries: "GET"]
    LotteryRecordsService lotteryRecordsService
    
    def saveLotteryRecords(LotteryRecords lotteryRecords) {
        if (lotteryRecords.hasErrors()) {
            respond new ApiResult(code: 500, msg: "参数错误", data: lotteryRecords.errors)
            return
        }
        def result = lotteryRecordsService.save(lotteryRecords)
        respond new ApiResult(code: 200, msg: "保存成功", data: result)
    }

    // 查询中奖记录
    def queryLotteryRecords(User user, int page, int size) {
        def result = LotteryRecords.findAllByUserAndLotteryNotIsNull(user, [max: size, offset: (page - 1) * size])
        respond new ApiResult(code: 200, msg: "查询成功", data: [records: result, total: LotteryRecords.countByUser(user)])
    }

    // 返回指定数量的抽奖奖品
    def giveLotteries(int count) {
        def list = Lottery.list()
        def result = []
        for (int i = 0; i < count; i++) {
            def randomIndex = new Random().nextInt(list.size())
            result << list.remove(randomIndex)
        }
        respond new ApiResult(code: 200, msg: "抽奖成功", data: result)
    }

}
