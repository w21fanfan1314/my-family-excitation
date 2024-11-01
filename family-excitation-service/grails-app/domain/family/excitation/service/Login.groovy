package family.excitation.service

class Login {
    LoginType type = LoginType.PC
    String token
    Date dateCreated
    Date lastUpdated
    static belongsTo = [user:User]
    static constraints = {
        token unique: true
    }

    def beforeInsert(){
        if (!token) {
            token = generateToken()
        }
    }

    // 生成唯一的登录token
    static String generateToken(){
        return UUID.randomUUID().toString().md5()
    }

    @Override
    String toString() {
        return "[${type}]${user.name}(${token})"
    }
}

enum LoginType{
    PC,
    WEB,
    APP
}
