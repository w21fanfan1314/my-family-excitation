package family.excitation.service.train

import family.excitation.service.User

class UserAnswer {
    String answer
    Date dateCreated
    Date lastUpdated

    static transients = ['correct']
    static hasMany = [options: QuestionOption]
    static belongsTo = [question: Question, user: User]
    static constraints = {
        lastUpdated nullable: false
        answer nullable: true
        levelRecord nullable: true
    }
    static mapping = {
        sort 'dateCreated'
        order 'desc'
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
