package family.excitation.service


class Discipline {
    String name
    Date dateCreated
    Date lastUpdated
    static hasMany = [scores: Score, homeworkReports: HomeworkReport]
    static constraints = {
        name unique: true, maxSize: 50
        scores nullable: true, display: false
        homeworkReports nullable: true, display: false
    }

    @Override
    String toString() {
        return name
    }
}
