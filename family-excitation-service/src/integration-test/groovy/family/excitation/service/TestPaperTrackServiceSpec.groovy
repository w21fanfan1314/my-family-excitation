package family.excitation.service

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class TestPaperTrackServiceSpec extends Specification {

    TestPaperTrackService testPaperTrackService
    SessionFactory sessionFactory

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        //new TestPaperTrack(...).save(flush: true, failOnError: true)
        //new TestPaperTrack(...).save(flush: true, failOnError: true)
        //TestPaperTrack testPaperTrack = new TestPaperTrack(...).save(flush: true, failOnError: true)
        //new TestPaperTrack(...).save(flush: true, failOnError: true)
        //new TestPaperTrack(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        //testPaperTrack.id
    }

    void "test get"() {
        setupData()

        expect:
        testPaperTrackService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<TestPaperTrack> testPaperTrackList = testPaperTrackService.list(max: 2, offset: 2)

        then:
        testPaperTrackList.size() == 2
        assert false, "TODO: Verify the correct instances are returned"
    }

    void "test count"() {
        setupData()

        expect:
        testPaperTrackService.count() == 5
    }

    void "test delete"() {
        Long testPaperTrackId = setupData()

        expect:
        testPaperTrackService.count() == 5

        when:
        testPaperTrackService.delete(testPaperTrackId)
        sessionFactory.currentSession.flush()

        then:
        testPaperTrackService.count() == 4
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        TestPaperTrack testPaperTrack = new TestPaperTrack()
        testPaperTrackService.save(testPaperTrack)

        then:
        testPaperTrack.id != null
    }
}
