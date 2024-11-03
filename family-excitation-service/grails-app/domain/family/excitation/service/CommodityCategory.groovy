package family.excitation.service

class CommodityCategory {
    String name
    Date dateCreated
    Date lastUpdated

    static hasMany = [commodities: Commodity]
    static constraints = {
        name unique: true
        commodities nullable: true
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
