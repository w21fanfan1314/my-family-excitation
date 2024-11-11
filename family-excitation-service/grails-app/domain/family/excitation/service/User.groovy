package family.excitation.service

import com.fasterxml.jackson.annotation.JsonIgnore
import family.excitation.service.train.UserAnswer

class User {
    String name
    String userName
    String password
    Date birthday
    String avatar
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
                      mediaDataList: MediaData, userAnswers: UserAnswer]
    static constraints = {
        name maxSize: 30
        userName unique: true, maxSize: 30
        password minSize: 6, maxSize: 16
        birthday nullable: true
        avatar nullable: true
        logins nullable: true
        records nullable: true
        statisitics nullable: true
        scores nullable: true
        orders nullable: true
        releases nullable: true
        mediaDataList nullable: true
        userAnswers nullable: true
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