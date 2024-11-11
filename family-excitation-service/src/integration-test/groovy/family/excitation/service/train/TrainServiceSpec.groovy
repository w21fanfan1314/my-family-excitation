package family.excitation.service.train

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class TrainServiceSpec extends Specification {

    TrainService trainService
    SessionFactory sessionFactory

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        //new Train(...).save(flush: true, failOnError: true)
        //new Train(...).save(flush: true, failOnError: true)
        //Train train = new Train(...).save(flush: true, failOnError: true)
        //new Train(...).save(flush: true, failOnError: true)
        //new Train(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        //train.id
    }

    void "test get"() {
        setupData()

        expect:
        trainService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<Train> trainList = trainService.list(max: 2, offset: 2)

        then:
        trainList.size() == 2
        assert false, "TODO: Verify the correct instances are returned"
    }

    void "test count"() {
        setupData()

        expect:
        trainService.count() == 5
    }

    void "test delete"() {
        Long trainId = setupData()

        expect:
        trainService.count() == 5

        when:
        trainService.delete(trainId)
        sessionFactory.currentSession.flush()

        then:
        trainService.count() == 4
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        Train train = new Train()
        trainService.save(train)

        then:
        train.id != null
    }
}
