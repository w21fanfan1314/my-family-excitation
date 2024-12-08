package family.excitation.service

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class AppConfigServiceSpec extends Specification {

    AppConfigService appConfigService
    SessionFactory sessionFactory

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        //new AppConfig(...).save(flush: true, failOnError: true)
        //new AppConfig(...).save(flush: true, failOnError: true)
        //AppConfig appConfig = new AppConfig(...).save(flush: true, failOnError: true)
        //new AppConfig(...).save(flush: true, failOnError: true)
        //new AppConfig(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        //appConfig.id
    }

    void "test get"() {
        setupData()

        expect:
        appConfigService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<AppConfig> appConfigList = appConfigService.list(max: 2, offset: 2)

        then:
        appConfigList.size() == 2
        assert false, "TODO: Verify the correct instances are returned"
    }

    void "test count"() {
        setupData()

        expect:
        appConfigService.count() == 5
    }

    void "test delete"() {
        Long appConfigId = setupData()

        expect:
        appConfigService.count() == 5

        when:
        appConfigService.delete(appConfigId)
        sessionFactory.currentSession.flush()

        then:
        appConfigService.count() == 4
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        AppConfig appConfig = new AppConfig()
        appConfigService.save(appConfig)

        then:
        appConfig.id != null
    }
}
