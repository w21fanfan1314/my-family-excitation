package family.excitation.service

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class LotteryRecordsServiceSpec extends Specification {

    LotteryRecordsService lotteryRecordsService
    SessionFactory sessionFactory

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        //new LotteryRecords(...).save(flush: true, failOnError: true)
        //new LotteryRecords(...).save(flush: true, failOnError: true)
        //LotteryRecords lotteryRecords = new LotteryRecords(...).save(flush: true, failOnError: true)
        //new LotteryRecords(...).save(flush: true, failOnError: true)
        //new LotteryRecords(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        //lotteryRecords.id
    }

    void "test get"() {
        setupData()

        expect:
        lotteryRecordsService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<LotteryRecords> lotteryRecordsList = lotteryRecordsService.list(max: 2, offset: 2)

        then:
        lotteryRecordsList.size() == 2
        assert false, "TODO: Verify the correct instances are returned"
    }

    void "test count"() {
        setupData()

        expect:
        lotteryRecordsService.count() == 5
    }

    void "test delete"() {
        Long lotteryRecordsId = setupData()

        expect:
        lotteryRecordsService.count() == 5

        when:
        lotteryRecordsService.delete(lotteryRecordsId)
        sessionFactory.currentSession.flush()

        then:
        lotteryRecordsService.count() == 4
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        LotteryRecords lotteryRecords = new LotteryRecords()
        lotteryRecordsService.save(lotteryRecords)

        then:
        lotteryRecords.id != null
    }
}
