package family.excitation.service

import grails.testing.web.interceptor.InterceptorUnitTest
import spock.lang.Specification

class ApiAuthInterceptorSpec extends Specification implements InterceptorUnitTest<ApiAuthInterceptor> {

    def setup() {
    }

    def cleanup() {

    }

    void "Test apiAuth interceptor matching"() {
        when:"A request matches the interceptor"
            withRequest(controller:"apiAuth")

        then:"The interceptor does match"
            interceptor.doesMatch()
    }
}
