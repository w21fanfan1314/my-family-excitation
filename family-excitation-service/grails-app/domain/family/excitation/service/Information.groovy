package family.excitation.service

class Information {
    InformationType type = InformationType.NOTICE
    String title
    String content
    Date dateCreated
    Date lastUpdated

    static constraints = {
        title maxSize: 200
    }

    static mapping = {
        sort 'dateCreated'
        order 'desc'
        content sqlType: 'text'
    }

    @Override
    String toString() {
        return title
    }
}

enum InformationType {
    // 公告
    NOTICE,
    // 提醒
    REMIND,
    // 订单
    ORDER,
}
