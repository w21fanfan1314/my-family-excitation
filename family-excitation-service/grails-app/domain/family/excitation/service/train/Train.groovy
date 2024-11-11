package family.excitation.service.train

class Train {
    String name
    Date dateCreated
    Date lastUpdated

    static hasMany = [levels: TrainLevel]
    static constraints = {
        name unique: true
        levels nullable: true
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
