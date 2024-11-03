package family.excitation.service

class Commodity {
    String name
    // 人民币的价格
    Double price
    String imageUrl
    // 外部参考链接，拼多多，淘宝网页地址
    String referenceUrl
    Date dateCreated
    Date lastUpdated

    static belongsTo = [category: CommodityCategory]
    static hasMany = [orders: Order]
    static constraints = {
        name unique: true, maxSize: 300
        price min: 0d
        imageUrl nullable: true, url: true, maxSize: 500
        referenceUrl nullable: true, url: true, maxSize: 1000
        orders nullable: true
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
