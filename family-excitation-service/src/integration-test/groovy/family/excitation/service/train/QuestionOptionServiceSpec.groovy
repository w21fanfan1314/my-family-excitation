package family.excitation.service.train

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class QuestionOptionServiceSpec extends Specification {

    QuestionOptionService questionOptionService
    SessionFactory sessionFactory

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        //new QuestionOption(...).save(flush: true, failOnError: true)
        //new QuestionOption(...).save(flush: true, failOnError: true)
        //QuestionOption questionOption = new QuestionOption(...).save(flush: true, failOnError: true)
        //new QuestionOption(...).save(flush: true, failOnError: true)
        //new QuestionOption(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        //questionOption.id
    }

    void "test get"() {
        setupData()

        expect:
        questionOptionService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<QuestionOption> questionOptionList = questionOptionService.list(max: 2, offset: 2)

        then:
        questionOptionList.size() == 2
        assert false, "TODO: Verify the correct instances are returned"
    }

    void "test count"() {
        setupData()

        expect:
        questionOptionService.count() == 5
    }

    void "test delete"() {
        Long questionOptionId = setupData()

        expect:
        questionOptionService.count() == 5

        when:
        questionOptionService.delete(questionOptionId)
        sessionFactory.currentSession.flush()

        then:
        questionOptionService.count() == 4
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        QuestionOption questionOption = new QuestionOption()
        questionOptionService.save(questionOption)

        then:
        questionOption.id != null
    }
}
