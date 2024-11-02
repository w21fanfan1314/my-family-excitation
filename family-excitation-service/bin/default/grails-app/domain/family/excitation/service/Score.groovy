package family.excitation.service

class Score {
    String level
    Integer score = 0
    Date dateCreated
    Date lastUpdated

    static belongsTo = [discipline:Discipline, user: User]
    static constraints = {
        level inList: ['A+', 'A', 'B', 'C', 'D'], maxSize: 2
        score min: 0
    }

    static mapping = {
        sort 'dateCreated'
        order 'desc'
    }

    def beforeInsert() {
        if (score == 0) {
            // 根据level计算
            switch (level) {
                case 'A':
                    score = 100
                    break
                case 'B':
                    score = 4
                    break
                case 'C':
                    score = 3
                    break
                case 'D':
                    score = 2
                    break
            }
        }
    }

    @Override
    String toString() {
        return "${score}分(${level})"
    }
}
