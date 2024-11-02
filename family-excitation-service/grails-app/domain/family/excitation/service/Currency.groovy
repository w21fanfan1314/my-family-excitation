package family.excitation.service

class Currency {
    String name
    String symbol
    Date dateCreated
    Date lastUpdated

    static hasMany = [records: UserRecord]
    static constraints = {
        name unique: true, maxSize: 32
        symbol nullable: true, maxSize: 2
        records nullable: true
    }

    @Override
    String toString() {
        return name
    }
}
