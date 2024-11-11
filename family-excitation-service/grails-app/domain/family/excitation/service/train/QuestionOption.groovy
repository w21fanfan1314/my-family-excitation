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
        sort 'dateCreated'
        order 'desc'
    }


    @Override
    String toString() {
        return option
    }

}
