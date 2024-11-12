package family.excitation.service.train

import family.excitation.service.User

import java.text.SimpleDateFormat

class UserAnswer {
    String answerId
    String answer
    Date dateCreated
    Date lastUpdated

    static transients = ['correct']
    static hasMany = [options: QuestionOption]
    static belongsTo = [question: Question, user: User]
    static constraints = {
        answerId maxSize: 255
        options nullable: true
        answer nullable: true
    }
    static mapping = {
        sort 'dateCreated'
        order 'desc'
    }

    static def generateAnswerId() {
        return new SimpleDateFormat('yyyyMMddHHmmssSSS').format(new Date())
    }

    def getCorrect() {
        if (question.type == QuestionType.SINGLE || question.type == QuestionType.JUDGE) {
            return (options?.size() > 0 && options[0].isRight)
        } else if (question.type == QuestionType.MULTIPLE) {
            if (options?.size() > 0 && options.size() == question.rightOption?.size()) {
                for (option in options) {
                    if (!option.isRight) {
                        return false
                    }
                }
                return true
            } else {
                return false
            }
        } else if (question.type == QuestionType.ANSWER) {
            return answer?.equalsIgnoreCase(question.answer)
        }
        return false
    }

    @Override
    String toString() {
        return "${user} ${question}"
    }
}
