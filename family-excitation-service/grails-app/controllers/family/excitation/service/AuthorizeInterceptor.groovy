package family.excitation.service

import family.excitation.service.api.ApiResult
import grails.util.Environment


class AuthorizeInterceptor {

    AuthorizeInterceptor() {
        matchAll()
                .excludes(controller: ~/.*?Api/)
    }

    boolean before() {
        if (Environment.current == Environment.PRODUCTION) {
            // 判断SESSION中是否有登录信息
            Login login = session.login
            if (!login || login.user.role != UserRole.ADMIN || !Login.exists(login.id)) {
                // 登录信息不存在，则跳转到登录页面
                redirect(controller: 'userApi', action: 'login')
                return false
            }
        }
        return true
    }

    boolean after() { true }

    void afterView() {
        // no-op
    }
}
