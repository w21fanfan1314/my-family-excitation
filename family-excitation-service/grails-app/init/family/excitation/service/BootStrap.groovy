package family.excitation.service

class BootStrap {
    UserService userService
    CurrencyService currencyService
    DisciplineService disciplineService

    def init = { servletContext ->
        // 创建一个管理员
        userService.save(new User(name: 'admin', userName: 'admin', password: 'asdf.1234', role: UserRole.ADMIN, position: UserPosition.PARENT))
        userService.save(new User(name: '刘泯铄', userName: 'marvin', password: '123456', role: UserRole.USER, position: UserPosition.CHILD))
        userService.save(new User(name: '王泯泽', userName: 'martin', password: '123456', role: UserRole.USER, position: UserPosition.CHILD))

        currencyService.save(new Currency(name: '人民币', symbol: '¥'))
        currencyService.save(new Currency(name: '卓越币', symbol: '¥'))

        disciplineService.save(new Discipline(name: '数学'))
        disciplineService.save(new Discipline(name: '语文'))
        disciplineService.save(new Discipline(name: '英语'))
        
    }
    def destroy = {
    }
}
