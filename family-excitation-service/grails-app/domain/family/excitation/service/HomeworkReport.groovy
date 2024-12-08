package family.excitation.service

class HomeworkReport {
    // 成绩
    String score
    // 数量
    int count = 1
    // 状态
    HomeworkReportStatus status = HomeworkReportStatus.SUBMITTED
    Date dateCreated
    Date lastUpdated

    static belongsTo = [discipline: Discipline, user: User]
    static constraints = {
        score inList: ['A+', 'A', 'B', 'C', 'D']
        count min: 1
    }

    def beforeUpdate() {
        // 如果已通过，则自动加入成绩中
        if (status === HomeworkReportStatus.PASSED) {
            Score.withTransaction {
                // 加入成绩中
                def scores = []
                for (def i = 0; i < count ; i ++) {
                    scores << new Score(user: user, level: score, award: AppConfig.instance.aAward, awardCurrency: AppConfig.instance.currency)
                }
                Score.saveAll(scores)
            }
        }
    }
}

// 状态
enum HomeworkReportStatus {
    // 已提交
    SUBMITTED,
    // 已通过
    PASSED,
    // 未通过
    CORRECTED

    @Override
    String toString() {
        switch (this) {
            case SUBMITTED:
                return '已提交'
            case PASSED:
                return '已通过'
            case CORRECTED:
                return '未通过'
            default:
                return '未知'
        }
    }
}
