package family.excitation.service.train

import family.excitation.service.User

import java.text.SimpleDateFormat

class UserAnswer {
    // 如果是选项题， 则存入所选的ID用','分割
    String answer
    Date dateCreated
    Date lastUpdated

    static transients = ['correct', 'userSelectedOptions']
    static belongsTo = [question: Question, user: User, transcript: Transcript]
    static constraints = {
        answer nullable: true
    }
    static mapping = {
        sort 'dateCreated'
        order 'desc'
    }


    def getCorrect() {
        if (question.type == QuestionType.SINGLE ||
                question.type == QuestionType.JUDGE ||
                question.type == QuestionType.MULTIPLE) {
            return answerGetOptions().findAll { it.isRight}.size() == question.rightOption?.size()
        } else {
            return answer?.equalsIgnoreCase(question.answer)
        }
    }

    def getUserSelectedOptions() {
        if (question.type == QuestionType.SINGLE ||
                question.type == QuestionType.JUDGE ||
                question.type == QuestionType.MULTIPLE) {
            return answerGetOptions()
        } else {
            return new QuestionOption(option: answer)
        }
    }

    def answerGetOptions() {
        def userSelectedOptions = []
        if (answer) {
            def optionIds = answer.split(',')
            for (optionId in optionIds) {
                def option = question.options?.find { it.id.toString() == optionId }
                if (!option) {
                    continue
                }
                userSelectedOptions << option
            }
        }
        return userSelectedOptions
    }

    @Override
    String toString() {
        return "${user} ${question}"
    }
}
