package family.excitation.service

class LotteryRecords {
    Date dateCreated
    Date lastUpdated

    static belongsTo = [user: User, lottery: Lottery]
    static constraints = {
        dateCreated nullable: false
        lottery nullable: true
    }

    def beforeInsert() {
        if (lottery?.type == LotteryType.AMOUNT) {
            UserRecord.withTransaction {
                new UserRecord(user: user, currency: lottery.currency, amount: lottery.amount, recordType: UserRecordType.LOTTERY, content: "抽奖获得${lottery.amount}元")
                    .save(flush: true)
            }
        }
    }

    static mapping = {
        sort 'dateCreated'
        order 'desc'
    }

    @Override
    String toString() {
        return "${user}抽奖获得了${lottery}"
    }
}
