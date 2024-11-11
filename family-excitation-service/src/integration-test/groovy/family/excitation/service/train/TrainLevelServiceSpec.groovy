package family.excitation.service.train

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class TrainLevelServiceSpec extends Specification {

    TrainLevelService trainLevelService
    SessionFactory sessionFactory

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        //new TrainLevel(...).save(flush: true, failOnError: true)
        //new TrainLevel(...).save(flush: true, failOnError: true)
        //TrainLevel trainLevel = new TrainLevel(...).save(flush: true, failOnError: true)
        //new TrainLevel(...).save(flush: true, failOnError: true)
        //new TrainLevel(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        //trainLevel.id
    }

    void "test get"() {
        setupData()

        expect:
        trainLevelService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<TrainLevel> trainLevelList = trainLevelService.list(max: 2, offset: 2)

        then:
        trainLevelList.size() == 2
        assert false, "TODO: Verify the correct instances are returned"
    }

    void "test count"() {
        setupData()

        expect:
        trainLevelService.count() == 5
    }

    void "test delete"() {
        Long trainLevelId = setupData()

        expect:
        trainLevelService.count() == 5

        when:
        trainLevelService.delete(trainLevelId)
        sessionFactory.currentSession.flush()

        then:
        trainLevelService.count() == 4
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        TrainLevel trainLevel = new TrainLevel()
        trainLevelService.save(trainLevel)

        then:
        trainLevel.id != null
    }
}
