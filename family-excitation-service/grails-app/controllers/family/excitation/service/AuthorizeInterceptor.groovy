package family.excitation.service

import family.excitation.service.api.ApiResult


class AuthorizeInterceptor {

    AuthorizeInterceptor() {
        matchAll()
                .except(controller: /.*?Api/)
    }

    boolean before() {
        // 判断SESSION中是否有登录信息
        Login login = session.login
        if (!login || login.user.role != UserRole.ADMIN || !Login.exists(login.id)) {
            // 登录信息不存在，则跳转到登录页面
            redirect(controller: 'userApi', action: 'login')
            return false
        }
        return true
    }

    boolean after() { true }

    void afterView() {
        // no-op
    }
}
