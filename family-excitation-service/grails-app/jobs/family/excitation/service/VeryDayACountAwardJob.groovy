package family.excitation.service

class VeryDayACountAwardJob {
    static triggers = {
      // 每天凌晨0点执行一次
      cron name: 'veryDayACountAwardJob', cronExpression: '0 0 0 * * ?'
    }

    UserRecordService userRecordService

    def execute() {
        if (AppConfig.instance.aCount == 0 ||
                AppConfig.instance.aAwardAgain == 0 || AppConfig.instance.currency == null) {
            return
        }

        // 统计上一天获得A或者A+的数量
        def cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -1)
        def start = cal.getTime()
        def end = new Date()

        def users = User.list()
        users.each {
            def count = Score.countByLevelInListAndUserAndDateCreatedBetween(['A', 'A+'], it, start, end)
            if (count >= AppConfig.instance.aCount) {
                userRecordService.save(new UserRecord(amount: AppConfig.instance.aAwardAgain,
                        currency: AppConfig.instance.currency, user: it, content: '三星奖励', type: UserRecordType.AWARD
                ))
            }
        }
    }
}
