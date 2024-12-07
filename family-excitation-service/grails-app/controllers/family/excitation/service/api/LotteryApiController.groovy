package family.excitation.service.api

import family.excitation.service.Login
import family.excitation.service.Lottery
import family.excitation.service.LotteryRecords
import family.excitation.service.LotteryRecordsService
import family.excitation.service.User

class LotteryApiController {
    static allowedMethods = [saveLotteryRecords: "GET", queryLotteryRecords: "GET", giveLotteries: "GET"]
    LotteryRecordsService lotteryRecordsService

    def saveLotteryRecords(Lottery lottery) {
        Login login = request.getAttribute('user_login') as Login
        def result = lotteryRecordsService.save(new LotteryRecords(user: login.user, lottery: lottery))
        if (result) {
            respond new ApiResult(code: 200, msg: "保存成功", data: result)
        } else {
            respond  new ApiResult(code: 500, msg: "保存失败")
        }
    }

    // 查询中奖记录
    def queryLotteryRecords(User user, int page, int size) {
        def result = LotteryRecords.createCriteria().list {
            eq("user", user)
            isNotNull('lottery')
            maxResults(size)
            firstResult(page * size)
        }
        respond new ApiResult(code: 200, msg: "查询成功", data: [records: result, total: LotteryRecords.countByUserAndLotteryIsNotNull(user)])
    }

    // 返回指定数量的抽奖奖品
    def giveLotteries(int count) {
        def list = Lottery.list()
        def result = []
        for (int i = 0; i < count; i++) {
            if (list.empty) {
                break
            }
            def randomIndex = new Random().nextInt(list.size())
            result << list.remove(randomIndex)
        }
        respond new ApiResult(code: 200, msg: "抽奖成功", data: result)
    }

}
