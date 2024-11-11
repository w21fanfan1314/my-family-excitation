package family.excitation.service.train

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class UserAnswerServiceSpec extends Specification {

    UserAnswerService userAnswerService
    SessionFactory sessionFactory

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        //new UserAnswer(...).save(flush: true, failOnError: true)
        //new UserAnswer(...).save(flush: true, failOnError: true)
        //UserAnswer userAnswer = new UserAnswer(...).save(flush: true, failOnError: true)
        //new UserAnswer(...).save(flush: true, failOnError: true)
        //new UserAnswer(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        //userAnswer.id
    }

    void "test get"() {
        setupData()

        expect:
        userAnswerService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<UserAnswer> userAnswerList = userAnswerService.list(max: 2, offset: 2)

        then:
        userAnswerList.size() == 2
        assert false, "TODO: Verify the correct instances are returned"
    }

    void "test count"() {
        setupData()

        expect:
        userAnswerService.count() == 5
    }

    void "test delete"() {
        Long userAnswerId = setupData()

        expect:
        userAnswerService.count() == 5

        when:
        userAnswerService.delete(userAnswerId)
        sessionFactory.currentSession.flush()

        then:
        userAnswerService.count() == 4
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        UserAnswer userAnswer = new UserAnswer()
        userAnswerService.save(userAnswer)

        then:
        userAnswer.id != null
    }
}
