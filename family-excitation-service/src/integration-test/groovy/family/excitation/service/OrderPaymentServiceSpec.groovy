package family.excitation.service

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class OrderPaymentServiceSpec extends Specification {

    OrderPaymentService orderPaymentService
    SessionFactory sessionFactory

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        //new OrderPayment(...).save(flush: true, failOnError: true)
        //new OrderPayment(...).save(flush: true, failOnError: true)
        //OrderPayment orderPayment = new OrderPayment(...).save(flush: true, failOnError: true)
        //new OrderPayment(...).save(flush: true, failOnError: true)
        //new OrderPayment(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        //orderPayment.id
    }

    void "test get"() {
        setupData()

        expect:
        orderPaymentService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<OrderPayment> orderPaymentList = orderPaymentService.list(max: 2, offset: 2)

        then:
        orderPaymentList.size() == 2
        assert false, "TODO: Verify the correct instances are returned"
    }

    void "test count"() {
        setupData()

        expect:
        orderPaymentService.count() == 5
    }

    void "test delete"() {
        Long orderPaymentId = setupData()

        expect:
        orderPaymentService.count() == 5

        when:
        orderPaymentService.delete(orderPaymentId)
        sessionFactory.currentSession.flush()

        then:
        orderPaymentService.count() == 4
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        OrderPayment orderPayment = new OrderPayment()
        orderPaymentService.save(orderPayment)

        then:
        orderPayment.id != null
    }
}
