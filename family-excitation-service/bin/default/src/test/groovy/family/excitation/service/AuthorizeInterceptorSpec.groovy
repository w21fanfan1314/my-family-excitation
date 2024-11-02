package family.excitation.service

import grails.testing.web.interceptor.InterceptorUnitTest
import spock.lang.Specification

class AuthorizeInterceptorSpec extends Specification implements InterceptorUnitTest<AuthorizeInterceptor> {

    def setup() {
    }

    def cleanup() {

    }

    void "Test authorize interceptor matching"() {
        when:"A request matches the interceptor"
            withRequest(controller:"authorize")

        then:"The interceptor does match"
            interceptor.doesMatch()
    }
}
