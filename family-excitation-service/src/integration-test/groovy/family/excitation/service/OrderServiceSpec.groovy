package family.excitation.service

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class OrderServiceSpec extends Specification {

    OrderService orderService
    SessionFactory sessionFactory

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        //new Order(...).save(flush: true, failOnError: true)
        //new Order(...).save(flush: true, failOnError: true)
        //Order order = new Order(...).save(flush: true, failOnError: true)
        //new Order(...).save(flush: true, failOnError: true)
        //new Order(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        //order.id
    }

    void "test get"() {
        setupData()

        expect:
        orderService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<Order> orderList = orderService.list(max: 2, offset: 2)

        then:
        orderList.size() == 2
        assert false, "TODO: Verify the correct instances are returned"
    }

    void "test count"() {
        setupData()

        expect:
        orderService.count() == 5
    }

    void "test delete"() {
        Long orderId = setupData()

        expect:
        orderService.count() == 5

        when:
        orderService.delete(orderId)
        sessionFactory.currentSession.flush()

        then:
        orderService.count() == 4
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        Order order = new Order()
        orderService.save(order)

        then:
        order.id != null
    }
}
