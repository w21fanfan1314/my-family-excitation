package family.excitation.service.train

class QuestionOption {
    String option
    boolean isRight = false
    Date dateCreated
    Date lastUpdated

    static belongsTo = [question:Question]
    static constraints = {
        option maxSize: 200
    }

    static mapping = {
        table "train_question_option"
        sort 'dateCreated'
        order 'desc'
        option column: "option_content"
    }


    @Override
    String toString() {
        return "[${isRight}]${option}"
    }

}
