package family.excitation.service

class UserRecord {
    UserRecordType recordType = UserRecordType.CONSUME
    String content
    Double amount = 0
    Date dateCreated
    Date lastUpdated
    User creator

    static belongsTo = [user:User]
    static constraints = {
        content nullable: true, maxSize: 500, widget: 'textarea'
    }

    static mapping = {
        sort 'dateCreated'
        order 'desc'
    }

    @Override
    String toString() {
        return "${user?.name}在${dateCreated}进行了${recordType}，${Math.abs(amount)}元"
    }
}

enum UserRecordType {
    // 消费
    CONSUME,
    // 奖励
    AWARD,
    // 充值
    RECHARGE,
    // 处罚
    PUNISH

    @Override
    String toString() {
        switch (this) {
            case CONSUME:
                return "消费"
            case AWARD:
                return "奖励"
            case RECHARGE:
                return "充值"
            case PUNISH:
                return "处罚"
            default:
                return "未知"
        }
    }
}
