package family.excitation.service

class UserRecord {
    UserRecordType recordType = UserRecordType.CONSUME
    String content
    Double amount = 0
    Date dateCreated
    Date lastUpdated

    static belongsTo = [user:User, currency: Currency]
    static constraints = {
        recordType nullable: false
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
    PUNISH,
    // 转入
    TRANSFER,
    // 转出
    TRANSFER_OUT,
    // 利息
    INTEREST

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
            case TRANSFER:
                return "转入"
            case TRANSFER_OUT:
                return "转出"
            case INTEREST:
                return "利息"
            default:
                return "未知"
        }
    }
}
