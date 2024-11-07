package family.excitation.service

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class ReleaseInformationServiceSpec extends Specification {

    ReleaseInformationService releaseInformationService
    SessionFactory sessionFactory

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        //new ReleaseInformation(...).save(flush: true, failOnError: true)
        //new ReleaseInformation(...).save(flush: true, failOnError: true)
        //ReleaseInformation releaseInformation = new ReleaseInformation(...).save(flush: true, failOnError: true)
        //new ReleaseInformation(...).save(flush: true, failOnError: true)
        //new ReleaseInformation(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        //releaseInformation.id
    }

    void "test get"() {
        setupData()

        expect:
        releaseInformationService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<ReleaseInformation> releaseInformationList = releaseInformationService.list(max: 2, offset: 2)

        then:
        releaseInformationList.size() == 2
        assert false, "TODO: Verify the correct instances are returned"
    }

    void "test count"() {
        setupData()

        expect:
        releaseInformationService.count() == 5
    }

    void "test delete"() {
        Long releaseInformationId = setupData()

        expect:
        releaseInformationService.count() == 5

        when:
        releaseInformationService.delete(releaseInformationId)
        sessionFactory.currentSession.flush()

        then:
        releaseInformationService.count() == 4
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        ReleaseInformation releaseInformation = new ReleaseInformation()
        releaseInformationService.save(releaseInformation)

        then:
        releaseInformation.id != null
    }
}
