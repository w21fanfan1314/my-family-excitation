package family.excitation.service

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class MediaDataServiceSpec extends Specification {

    MediaDataService mediaDataService
    SessionFactory sessionFactory

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        //new MediaData(...).save(flush: true, failOnError: true)
        //new MediaData(...).save(flush: true, failOnError: true)
        //MediaData mediaData = new MediaData(...).save(flush: true, failOnError: true)
        //new MediaData(...).save(flush: true, failOnError: true)
        //new MediaData(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        //mediaData.id
    }

    void "test get"() {
        setupData()

        expect:
        mediaDataService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<MediaData> mediaDataList = mediaDataService.list(max: 2, offset: 2)

        then:
        mediaDataList.size() == 2
        assert false, "TODO: Verify the correct instances are returned"
    }

    void "test count"() {
        setupData()

        expect:
        mediaDataService.count() == 5
    }

    void "test delete"() {
        Long mediaDataId = setupData()

        expect:
        mediaDataService.count() == 5

        when:
        mediaDataService.delete(mediaDataId)
        sessionFactory.currentSession.flush()

        then:
        mediaDataService.count() == 4
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        MediaData mediaData = new MediaData()
        mediaDataService.save(mediaData)

        then:
        mediaData.id != null
    }
}
