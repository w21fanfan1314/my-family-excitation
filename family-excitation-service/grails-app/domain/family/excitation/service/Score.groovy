package family.excitation.service

import org.apache.commons.lang3.RandomUtils

class Score {
    String level = ""
    Integer score = 0
    // 奖励
    Double award = 0.0
    Currency awardCurrency
    Date dateCreated
    Date lastUpdated

    static belongsTo = [discipline:Discipline, user: User]
    static constraints = {
        level inList: ['A+', 'A', 'B', 'C', 'D'], maxSize: 2, blank: true, nullable: true
        score min: 0
        award min: 0d
        awardCurrency nullable: true
    }

    static mapping = {
        sort 'dateCreated'
        order 'desc'
    }

    def beforeInsert() {
        if (level && score == 0) {
            // 根据level计算
            switch(level) {
                case 'A+':
                    score = 100
                    break
                case 'A':
                    score = RandomUtils.nextInt(85, 100)
                    break
                case 'B':
                    score = RandomUtils.nextInt(75, 84)
                    break
                case 'C':
                    score = RandomUtils.nextInt(60, 74)
                    break
                case 'D':
                    score = RandomUtils.nextInt(0, 60)
                    break
            }
        } else if (!level && score > 0) {
            if (score >= 100) {
                level = 'A+'
            } else if (score < 100 && score >= 85) {
                level = 'A'
            } else if (score < 85 && score >= 75) {
                level = 'B'
            } else if (score < 75 && score >= 60) {
                level = 'C'
            } else {
                level = 'D'
            }
        }

        if (award == 0) {
            award = AppConfig.instance.aAward
        }

        if (!awardCurrency) {
            awardCurrency = AppConfig.instance.currency
        }

        if (award > 0 && awardCurrency) {
            UserRecord userRecord = new UserRecord(user: user, recordType: UserRecordType.AWARD, amount: award, content: "奖励${discipline.name}获得了${level}的成绩", currency: awardCurrency)
            UserRecord.withTransaction {
                userRecord.save()
            }
        }
    }

    @Override
    String toString() {
        return "${score}分(${level})"
    }
}
