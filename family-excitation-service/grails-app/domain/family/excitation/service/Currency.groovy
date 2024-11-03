package family.excitation.service

class Currency {
    String name
    String symbol
    // 针对人民币的汇率
    Double rate = 1
    // 利息
    Double interest = 0
    Date dateCreated
    Date lastUpdated

    static hasMany = [records: UserRecord]
    static constraints = {
        name unique: true, maxSize: 32
        symbol nullable: true, maxSize: 2
        records nullable: true
        rate min: 0d
        interest min: 0d
    }

    @Override
    String toString() {
        return name
    }
}
