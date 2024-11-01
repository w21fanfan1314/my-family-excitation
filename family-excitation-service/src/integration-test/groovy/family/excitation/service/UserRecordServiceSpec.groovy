package family.excitation.service

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class UserRecordServiceSpec extends Specification {

    UserRecordService userRecordService
    SessionFactory sessionFactory

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        //new UserRecord(...).save(flush: true, failOnError: true)
        //new UserRecord(...).save(flush: true, failOnError: true)
        //UserRecord userRecord = new UserRecord(...).save(flush: true, failOnError: true)
        //new UserRecord(...).save(flush: true, failOnError: true)
        //new UserRecord(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        //userRecord.id
    }

    void "test get"() {
        setupData()

        expect:
        userRecordService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<UserRecord> userRecordList = userRecordService.list(max: 2, offset: 2)

        then:
        userRecordList.size() == 2
        assert false, "TODO: Verify the correct instances are returned"
    }

    void "test count"() {
        setupData()

        expect:
        userRecordService.count() == 5
    }

    void "test delete"() {
        Long userRecordId = setupData()

        expect:
        userRecordService.count() == 5

        when:
        userRecordService.delete(userRecordId)
        sessionFactory.currentSession.flush()

        then:
        userRecordService.count() == 4
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        UserRecord userRecord = new UserRecord()
        userRecordService.save(userRecord)

        then:
        userRecord.id != null
    }
}
