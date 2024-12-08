package family.excitation.service

class AppConfig {
    // 获得a的奖励
    Double aAward = 2.0
    // 获得A次数，再次触发奖励
    Integer aCount = 3
    // 再次奖励的金额
    Double aAwardAgain = 2.0
    // 奖励使用的默认货币
    Currency currency
    // 每天增加的抽奖机会
    Integer lotteryChance = 3
    Date dateCreated
    Date lastUpdated

    static AppConfig instance

    def afterInsert() {
        instance = this
    }

    def afterUpdate() {
        instance = this
    }

    def afterDelete() {
        instance = new AppConfig()
    }

    static constraints = {
        aAward min: 0d
        aCount min: 0
        aAwardAgain min: 0d
        currency nullable: true
        lotteryChance min: 0
    }
}
