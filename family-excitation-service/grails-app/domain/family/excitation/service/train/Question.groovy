package family.excitation.service.train

import family.excitation.service.MediaData

class Question {
    String content
    String answer
    QuestionType type = QuestionType.SINGLE
    Date dateCreated
    Date lastUpdated

    static transients = ['rightOption']
    static belongsTo = [level: TrainLevel]
    static hasMany = [meidaDataList: MediaData, options: QuestionOption, userAnswers: UserAnswer]
    static constraints = {
        content maxSize: 1000
        answer nullable: true, blank: true, maxSize: 2000, widget: 'textarea'
        meidaDataList nullable: true
        options nullable: true
        userAnswers nullable: true
        level nullable: true
    }
    static mapping = {
        sort 'dateCreated'
        order 'desc'
    }



    def getRightOption() {
        if (type == QuestionType.SINGLE || type == QuestionType.JUDGE || type == QuestionType.MULTIPLE) {
            return options?.findAll { it.isRight }
        } else {
            return [new QuestionOption(option: answer, isRight: true)]
        }
    }



    @Override
    String toString() {
        return "[${type}]${content}"
    }
}

enum QuestionType {
    // 单选题
    SINGLE,
    // 多选题
    MULTIPLE,
    // 判断题
    JUDGE,
    // 问答题
    ANSWER

    @Override
    String toString() {
        switch (this) {
            case SINGLE:
                return '单选题'
            case MULTIPLE:
                return '多选题'
            case JUDGE:
                return '判断题'
            case ANSWER:
                return '问答题'
            default:
                return '未知'
        }
    }
}
