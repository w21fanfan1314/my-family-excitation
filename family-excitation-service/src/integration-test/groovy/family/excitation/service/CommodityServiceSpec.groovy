package family.excitation.service

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class CommodityServiceSpec extends Specification {

    CommodityService commodityService
    SessionFactory sessionFactory

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        //new Commodity(...).save(flush: true, failOnError: true)
        //new Commodity(...).save(flush: true, failOnError: true)
        //Commodity commodity = new Commodity(...).save(flush: true, failOnError: true)
        //new Commodity(...).save(flush: true, failOnError: true)
        //new Commodity(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        //commodity.id
    }

    void "test get"() {
        setupData()

        expect:
        commodityService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<Commodity> commodityList = commodityService.list(max: 2, offset: 2)

        then:
        commodityList.size() == 2
        assert false, "TODO: Verify the correct instances are returned"
    }

    void "test count"() {
        setupData()

        expect:
        commodityService.count() == 5
    }

    void "test delete"() {
        Long commodityId = setupData()

        expect:
        commodityService.count() == 5

        when:
        commodityService.delete(commodityId)
        sessionFactory.currentSession.flush()

        then:
        commodityService.count() == 4
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        Commodity commodity = new Commodity()
        commodityService.save(commodity)

        then:
        commodity.id != null
    }
}
