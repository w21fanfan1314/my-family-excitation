package family.excitation.service

class Discipline {
    String name
    Date dateCreated
    Date lastUpdated

    static hasMany = [scores: Score]
    static constraints = {
        name unique: true, maxSize: 50
        scores nullable: true
    }

    @Override
    String toString() {
        return name
    }
}
