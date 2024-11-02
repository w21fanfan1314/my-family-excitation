package family.excitation.service

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class AssetStatisticsServiceSpec extends Specification {

    AssetStatisticsService assetStatisticsService
    SessionFactory sessionFactory

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        //new AssetStatistics(...).save(flush: true, failOnError: true)
        //new AssetStatistics(...).save(flush: true, failOnError: true)
        //AssetStatistics assetStatistics = new AssetStatistics(...).save(flush: true, failOnError: true)
        //new AssetStatistics(...).save(flush: true, failOnError: true)
        //new AssetStatistics(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        //assetStatistics.id
    }

    void "test get"() {
        setupData()

        expect:
        assetStatisticsService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<AssetStatistics> assetStatisticsList = assetStatisticsService.list(max: 2, offset: 2)

        then:
        assetStatisticsList.size() == 2
        assert false, "TODO: Verify the correct instances are returned"
    }

    void "test count"() {
        setupData()

        expect:
        assetStatisticsService.count() == 5
    }

    void "test delete"() {
        Long assetStatisticsId = setupData()

        expect:
        assetStatisticsService.count() == 5

        when:
        assetStatisticsService.delete(assetStatisticsId)
        sessionFactory.currentSession.flush()

        then:
        assetStatisticsService.count() == 4
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        AssetStatistics assetStatistics = new AssetStatistics()
        assetStatisticsService.save(assetStatistics)

        then:
        assetStatistics.id != null
    }
}
