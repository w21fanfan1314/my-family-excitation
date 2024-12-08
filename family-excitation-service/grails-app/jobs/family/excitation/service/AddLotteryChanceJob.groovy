package family.excitation.service

import grails.util.Environment

class AddLotteryChanceJob {
    static triggers = {
        switch (Environment.current) {
            case Environment.DEVELOPMENT:
                // 每10秒执行一次
                simple name: 'addLotteryChanceJob', repeatInterval: 10000
                break
            case Environment.PRODUCTION:
                // 每天凌晨0点执行一次
                cron name: 'addLotteryChanceJob', cronExpression: '0 0 0 * * ?'
                break
        }
    }

    def execute() {
        // 给所有用户增加3个抽奖机会
        def users = User.list()
        users.each {
            it.lotteryChance += AppConfig.instance.lotteryChance
        }
        User.withNewTransaction {
            User.saveAll(users)
        }
    }
}
