package family.excitation.service.train

import family.excitation.service.Commons
import family.excitation.service.User
import family.excitation.service.UserRecord
import family.excitation.service.UserRecordType

class Transcript {
    Double score = 0
    Date dateCreated
    Date lastUpdated

    static transients = ['wrongs', 'isAward', 'starCount', 'hasAward']
    static belongsTo = [user: User, level: TrainLevel]
    static hasMany = [answers: UserAnswer]
    static constraints = {
        score min: 0d
        level nullable: true
    }

    static mapping = {
        table 'user_transcript'
        sort 'dateCreated'
        order 'desc'
    }

    def beforeInsert() {
        if (!score) {
            score = Math.floor(answers.findAll {it.correct}.size() / answers.size() * 100)
        }
    }

    def afterInsert() {
        if (hasAward) {
            updateUserRecord()
        }
    }

    /**
     * 判断是否可以获得奖励
     * @return
     */
    def getHasAward() {
        if (level) {
            def awardCount
            if (level.awardType == TrainLevelAwardType.SINGLE) {
                awardCount = Transcript.findAllByUserAndLevel(user, level).findAll {it.isAward()}.size()
            } else {
                def times = Commons.timeToRange(new Date().time)
                awardCount = Transcript.findAllByUserAndLevelAndDateCreatedBetween(user, level, times[0], times[1]).findAll {it.isAward()}.size()
            }
            return (level.award > 0 && this.isAward() &&  awardCount <= level.awardMaxCount)
        }
        return false
    }

    def getStarCount() {
        return Math.floor(score / 30)
    }

    // 统计错题
    def getWrongs() {
        return answers?.findAll { !it.correct }
    }

    def isAward() {
        return score >= 100
    }

    def updateUserRecord() {
        if (level.currency) {
            UserRecord.withTransaction {
                new UserRecord(user: user, currency: level.currency, amount: level.award, recordType: UserRecordType.AWARD, content: "${level?.train?.name}的第${level.level}关获得了${level.currency?.name}:${level.award}").save()
            }
        }
    }

    @Override
    String toString() {
        return "${dateCreated}${user?.name}成绩：${score}"
    }
}
