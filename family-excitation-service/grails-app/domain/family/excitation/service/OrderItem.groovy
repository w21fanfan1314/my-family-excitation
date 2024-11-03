package family.excitation.service

class OrderItem {
    String name
    String imageUrl
    Double price = 0
    Integer buyCount = 1
    String specification
    Commodity commodity
    Date dateCreated
    Date lastUpdated

    static belongsTo = [userOrder: Order]
    static constraints = {
        name nullable: false, blank: false
        imageUrl nullable: true, maxSize: 500
        price min: 0d
        buyCount min: 1
        specification nullable: true
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
