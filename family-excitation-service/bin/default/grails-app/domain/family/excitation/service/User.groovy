package family.excitation.service

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

    static hasMany = [logins: Login, records: UserRecord]
    static constraints = {
        name maxSize: 30
        userName unique: true, maxSize: 30
        password minSize: 6, maxSize: 16
        birthday nullable: true
        avatar nullable: true, url: true
        logins nullable: true
        records nullable: true
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