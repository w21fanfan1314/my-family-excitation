package family.excitation.service.train

class TrainLevel {
    int level = 1
    Double award = 0
    // -1 表示无限次数
    int awardMaxCount = -1
    Date dateCreated
    Date lastUpdated
    static transients = ['questionCount']
    static belongsTo = [train: Train]
    static hasMany = [questions: Question]
    static constraints = {
        level min: 1
        questions nullable: true
        award min: 0d
        awardMaxCount min: -1
    }
    static mapping = {
        sort 'level'
        order 'asc'
    }

    def getQuestionCount() {
        return questions?.size() ?: 0
    }

    String toString() {
        return "第${level}关"
    }
}
