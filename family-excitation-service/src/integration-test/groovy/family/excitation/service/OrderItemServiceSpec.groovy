package family.excitation.service

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class OrderItemServiceSpec extends Specification {

    OrderItemService orderItemService
    SessionFactory sessionFactory

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        //new OrderItem(...).save(flush: true, failOnError: true)
        //new OrderItem(...).save(flush: true, failOnError: true)
        //OrderItem orderItem = new OrderItem(...).save(flush: true, failOnError: true)
        //new OrderItem(...).save(flush: true, failOnError: true)
        //new OrderItem(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        //orderItem.id
    }

    void "test get"() {
        setupData()

        expect:
        orderItemService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<OrderItem> orderItemList = orderItemService.list(max: 2, offset: 2)

        then:
        orderItemList.size() == 2
        assert false, "TODO: Verify the correct instances are returned"
    }

    void "test count"() {
        setupData()

        expect:
        orderItemService.count() == 5
    }

    void "test delete"() {
        Long orderItemId = setupData()

        expect:
        orderItemService.count() == 5

        when:
        orderItemService.delete(orderItemId)
        sessionFactory.currentSession.flush()

        then:
        orderItemService.count() == 4
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        OrderItem orderItem = new OrderItem()
        orderItemService.save(orderItem)

        then:
        orderItem.id != null
    }
}
