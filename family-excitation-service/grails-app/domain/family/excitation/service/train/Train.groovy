package family.excitation.service.train

class Train {
    String name
    // 介绍
    String description
    String image
    String category
    Date dateCreated
    Date lastUpdated

    static hasMany = [levels: TrainLevel]
    static constraints = {
        name unique: true
        levels nullable: true
        description nullable: true, maxSize: 500, widget: 'textarea'
        image nullable: true, url: true
        category nullable: true
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
