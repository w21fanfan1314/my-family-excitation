package family.excitation.service

import com.fasterxml.jackson.annotation.JsonIgnore
import family.excitation.service.train.Transcript
import family.excitation.service.train.UserAnswer

class User {
    String name
    String userName
    String password
    Date birthday
    String avatar
    // 抽奖机会
    int lotteryChance = 0
    UserGender gender = UserGender.MALE
    UserRole role = UserRole.USER
    UserPosition position = UserPosition.CHILD
    Date dateCreated
    Date lastUpdated

    static hasMany = [logins: Login,
                      records: UserRecord,
                      statisitics:AssetStatistics,
                      scores: Score,
                      orders: Order,
                      releases: ReleaseInformation,
                      mediaDataList: MediaData, userAnswers: UserAnswer, transcripts: Transcript, paperTracks: TestPaperTrack]
    static constraints = {
        name maxSize: 30
        userName unique: true, maxSize: 30
        password minSize: 6, maxSize: 16
        birthday nullable: true
        avatar nullable: true
        lotteryChance min: 0
        logins nullable: true, display: false
        records nullable: true, display: false
        statisitics nullable: true, display: false
        scores nullable: true, display: false
        orders nullable: true, display: false
        releases nullable: true, display: false
        mediaDataList nullable: true, display: false
        userAnswers nullable: true, display: false
        transcripts nullable: true, display: false
        paperTracks nullable: true, display: false
    }

    static mapping = {
        sort 'dateCreated'
        order 'desc'
    }

    @Override
    String toString() {
        return "${name}(${userName})"
    }
}

enum UserRole {
    ADMIN, USER
}

enum UserGender {
    MALE, FEMALE
}

enum UserPosition {
    PARENT, CHILD
}