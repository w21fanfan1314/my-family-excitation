package family.excitation.service

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class LotteryServiceSpec extends Specification {

    LotteryService lotteryService
    SessionFactory sessionFactory

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        //new Lottery(...).save(flush: true, failOnError: true)
        //new Lottery(...).save(flush: true, failOnError: true)
        //Lottery lottery = new Lottery(...).save(flush: true, failOnError: true)
        //new Lottery(...).save(flush: true, failOnError: true)
        //new Lottery(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        //lottery.id
    }

    void "test get"() {
        setupData()

        expect:
        lotteryService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<Lottery> lotteryList = lotteryService.list(max: 2, offset: 2)

        then:
        lotteryList.size() == 2
        assert false, "TODO: Verify the correct instances are returned"
    }

    void "test count"() {
        setupData()

        expect:
        lotteryService.count() == 5
    }

    void "test delete"() {
        Long lotteryId = setupData()

        expect:
        lotteryService.count() == 5

        when:
        lotteryService.delete(lotteryId)
        sessionFactory.currentSession.flush()

        then:
        lotteryService.count() == 4
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        Lottery lottery = new Lottery()
        lotteryService.save(lottery)

        then:
        lottery.id != null
    }
}
