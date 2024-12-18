package family.excitation.service

class Commodity {
    String name
    // 人民币的价格
    Double price
    String imageUrl
    // 外部参考链接，拼多多，淘宝网页地址
    String referenceUrl
    String specification
    Date dateCreated
    Date lastUpdated

    static belongsTo = [category: CommodityCategory]
    static constraints = {
        name unique: true, maxSize: 300
        price min: 0d
        specification nullable: true
        imageUrl nullable: true, url: true, maxSize: 500
        referenceUrl nullable: true, url: true, maxSize: 1000
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
