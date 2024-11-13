package family.excitation.service.train

import family.excitation.service.Currency
import family.excitation.service.User

class TrainLevel {
    int level = 1
    Currency currency
    Double award = 0
    // -1 表示无限次数
    int awardMaxCount = -1
    TrainLevelAwardType awardType = TrainLevelAwardType.DAILY
    Date dateCreated
    Date lastUpdated
    static transients = ['questionCount']
    static belongsTo = [train: Train]
    static hasMany = [questions: Question, transcripts: Transcript]
    static constraints = {
        level min: 1
        questions nullable: true
        award min: 0d
        awardMaxCount min: -1
        currency nullable: true
        transcripts nullable: true
    }
    static mapping = {
        sort 'level'
        order 'asc'
    }

    def getQuestionCount() {
        return questions?.size() ?: 0
    }

    def bestTranscriptByUser(User user) {
        return transcripts.findAll { it.user.id == user.id}.max { it.starCount}
    }


    String toString() {
        return "第${level}关"
    }
}

enum TrainLevelAwardType {
    SINGLE,
    // 每天
    DAILY
}
