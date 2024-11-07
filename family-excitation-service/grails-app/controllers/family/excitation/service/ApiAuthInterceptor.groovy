package family.excitation.service

import family.excitation.service.api.ApiResult
import grails.converters.JSON


class ApiAuthInterceptor {

    ApiAuthInterceptor() {
        match(controller: /.*?Api/)
                .except(action: 'login')
    }

    boolean before() {
        def token = request.getHeader('app-token')
        if (token && Login.findByToken(token)) {
            return true
        } else {
            render new ApiResult(code: 401, msg: '未登录') as JSON
            return false
        }
    }

    boolean after() { true }

    void afterView() {
        // no-op
    }
}
