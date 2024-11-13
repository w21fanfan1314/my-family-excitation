package family.excitation.service

import family.excitation.service.api.ApiResult
import grails.converters.JSON


class ApiAuthInterceptor {

    ApiAuthInterceptor() {
        match(controller: /.*?Api/)
                .except(controler: 'userApi',action: 'login')
                .except(controler: 'releaseInformationApi',action: 'generateAll')
                .except(controler: 'mediaDataApi',action: 'show')
    }

    boolean before() {
        def token = request.getHeader('app-token')
        def login
        if (token && (login = Login.findByToken(token))) {
            request.setAttribute("user_login", login)
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
