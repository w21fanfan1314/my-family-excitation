package family.excitation.service

class BootStrap {
    UserService userService

    def init = { servletContext ->
        // 创建一个管理员
        userService.save(new User(name: 'admin', userName: 'admin', password: 'asdf.1234', role: UserRole.ADMIN, position: UserPosition.PARENT))
    }
    def destroy = {
    }
}
