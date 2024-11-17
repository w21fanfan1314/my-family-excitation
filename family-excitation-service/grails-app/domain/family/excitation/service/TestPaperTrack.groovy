package family.excitation.service

import java.text.SimpleDateFormat

class TestPaperTrack {
    String name
    TestPaperStatus status = TestPaperStatus.UNSTARTED
    // 总分
    Double totalScore = 100
    // 合格分数
    Double qualifiedScore = 80
    // 获得的分数
    Double score
    // 答卷开始时间
    Date startTime
    // 答卷结束时间
    Date endTime
    // 作答限制时间, 单位：分钟
    Integer answerLimitTime = 0
    // 满分奖励
    Double award
    // 实际奖励
    Double actualAward
    // 奖励使用的货币类型
    Currency currency
    Date dateCreated
    Date lastUpdated
    static belongsTo = [user: User]
    static constraints = {
        name nullable: true, maxSize: 500
        score nullable: true, min: 0d
        startTime nullable: true
        endTime nullable: true
        award nullable: true, min: 0d
        currency nullable: true
        answerLimitTime min: 0
        totalScore min: 0d
        qualifiedScore min: 0d
        actualAward nullable: true, min: 0d
    }

    static mapping = {
        sort 'dateCreated'
        order 'desc'
    }

    def beforeInsert() {
        if (!name) {
            name = "${user}的测试试卷-${new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())}"
        }
    }

    def beforeUpdate() {
        if (status == TestPaperStatus.ANSWERING) {
            startTime = new Date()
        } else if (status == TestPaperStatus.ENDED) {
            endTime = new Date()
                if (answerLimitTime > 0) {
                    if (endTime.time - startTime.time > answerLimitTime * 60 * 1000) {
                        status = TestPaperStatus.TIMEOUT
                    }
                }
        } else if (status === TestPaperStatus.QUALIFIED){
            if (score >= qualifiedScore) {
                    status = TestPaperStatus.QUALIFIED
                    if (currency && award > 0) {
                        // 根据分数分配奖励
                        actualAward = Math.round(award * (score / totalScore))
                    }
                } 
        }  else if (status == TestPaperStatus.RECEIVED) {
            if (currency && actualAward > 0) {
                UserRecord.withTransaction {
                    new UserRecord(user: user, currency: currency, amount: actualAward, content: "合格${name} 获得的奖励 ${currency.name}:${currency.symbol}${actualAward}元").save()
                }
            }
        }
    }

    @Override
    String toString() {
        return name
    }
}


enum TestPaperStatus {
    // 未开始作答
    UNSTARTED,
    // 正在作答
    ANSWERING,
    // 已结束作答
    ENDED,
    // 合格
    QUALIFIED,
    // 不合格
    UNQUALIFIED,
    // 超时未完成
    TIMEOUT,
    // 已领取奖励
    RECEIVED

    @Override
    String toString() {
        switch (this) {
            case UNSTARTED:
                return "未开始作答"
            case ANSWERING:
                return "正在作答"
            case ENDED:
                return "已结束作答"
            case QUALIFIED:
                return "合格"
            case UNQUALIFIED:
                return "不合格"
            case TIMEOUT:
                return "超时"
            case RECEIVED:
                return "已领取奖励"
        }
    }
}
