package family.excitation.service

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class CommodityCategoryServiceSpec extends Specification {

    CommodityCategoryService commodityCategoryService
    SessionFactory sessionFactory

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        //new CommodityCategory(...).save(flush: true, failOnError: true)
        //new CommodityCategory(...).save(flush: true, failOnError: true)
        //CommodityCategory commodityCategory = new CommodityCategory(...).save(flush: true, failOnError: true)
        //new CommodityCategory(...).save(flush: true, failOnError: true)
        //new CommodityCategory(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        //commodityCategory.id
    }

    void "test get"() {
        setupData()

        expect:
        commodityCategoryService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<CommodityCategory> commodityCategoryList = commodityCategoryService.list(max: 2, offset: 2)

        then:
        commodityCategoryList.size() == 2
        assert false, "TODO: Verify the correct instances are returned"
    }

    void "test count"() {
        setupData()

        expect:
        commodityCategoryService.count() == 5
    }

    void "test delete"() {
        Long commodityCategoryId = setupData()

        expect:
        commodityCategoryService.count() == 5

        when:
        commodityCategoryService.delete(commodityCategoryId)
        sessionFactory.currentSession.flush()

        then:
        commodityCategoryService.count() == 4
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        CommodityCategory commodityCategory = new CommodityCategory()
        commodityCategoryService.save(commodityCategory)

        then:
        commodityCategory.id != null
    }
}
