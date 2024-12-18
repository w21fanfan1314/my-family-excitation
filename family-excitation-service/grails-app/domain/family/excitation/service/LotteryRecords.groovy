package family.excitation.service

class LotteryRecords {
    Date dateCreated
    Date lastUpdated
    // 是否已经兑换
    boolean exchanged = false

    static belongsTo = [user: User, lottery: Lottery]
    static constraints = {
        exchanged nullable: false
        lottery nullable: true
    }

    def beforeInsert() {
        if (lottery?.type == LotteryType.AMOUNT) {
            UserRecord.withTransaction {
                new UserRecord(user: user, currency: lottery.currency, amount: lottery.amount, recordType: UserRecordType.LOTTERY, content: "抽奖获得${lottery.amount}元")
                        .save()
            }
            exchanged = true
        }
        if (user.lotteryChance > 0) {
            user.lotteryChance --
            User.withTransaction {
                user.save()
            }
        }
        // 减少奖品数量
        if (lottery?.count > 0) {
            lottery.count --
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

