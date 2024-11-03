package family.excitation.service

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class InformationServiceSpec extends Specification {

    InformationService informationService
    SessionFactory sessionFactory

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        //new Information(...).save(flush: true, failOnError: true)
        //new Information(...).save(flush: true, failOnError: true)
        //Information information = new Information(...).save(flush: true, failOnError: true)
        //new Information(...).save(flush: true, failOnError: true)
        //new Information(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        //information.id
    }

    void "test get"() {
        setupData()

        expect:
        informationService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<Information> informationList = informationService.list(max: 2, offset: 2)

        then:
        informationList.size() == 2
        assert false, "TODO: Verify the correct instances are returned"
    }

    void "test count"() {
        setupData()

        expect:
        informationService.count() == 5
    }

    void "test delete"() {
        Long informationId = setupData()

        expect:
        informationService.count() == 5

        when:
        informationService.delete(informationId)
        sessionFactory.currentSession.flush()

        then:
        informationService.count() == 4
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        Information information = new Information()
        informationService.save(information)

        then:
        information.id != null
    }
}
