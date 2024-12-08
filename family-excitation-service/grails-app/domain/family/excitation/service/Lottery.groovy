package family.excitation.service

class Lottery {
    String name
    String image
    Currency currency
    Double amount = 0
    LotteryType type = LotteryType.AMOUNT
    Date dateCreated
    Date lastUpdated

    static hasMany = [records: LotteryRecords]
    static constraints = {
        name minSize: 1, maxSize: 50
        image url: true
        amount min: 0d
        records nullable: true, display: false
        currency nullable: true
    }

    static mapping = {
        sort 'dateCreated'
        order 'desc'
    }

    @Override
    String toString() {
        return name
    }
}

enum LotteryType {
    // 金额
    AMOUNT,
    // 物品
    THING

    @Override
    String toString() {
        switch (this) {
            case AMOUNT:
                return "金额"
            case THING:
                return "物品"
            default:
                return "未知"
        }
    }
}
