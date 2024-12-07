package family.excitation.service

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class VideoItemServiceSpec extends Specification {

    VideoItemService videoItemService
    SessionFactory sessionFactory

    private Long setupData() {
        // TODO: Populate valid domain instances and return a valid ID
        //new VideoItem(...).save(flush: true, failOnError: true)
        //new VideoItem(...).save(flush: true, failOnError: true)
        //VideoItem videoItem = new VideoItem(...).save(flush: true, failOnError: true)
        //new VideoItem(...).save(flush: true, failOnError: true)
        //new VideoItem(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        //videoItem.id
    }

    void "test get"() {
        setupData()

        expect:
        videoItemService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<VideoItem> videoItemList = videoItemService.list(max: 2, offset: 2)

        then:
        videoItemList.size() == 2
        assert false, "TODO: Verify the correct instances are returned"
    }

    void "test count"() {
        setupData()

        expect:
        videoItemService.count() == 5
    }

    void "test delete"() {
        Long videoItemId = setupData()

        expect:
        videoItemService.count() == 5

        when:
        videoItemService.delete(videoItemId)
        sessionFactory.currentSession.flush()

        then:
        videoItemService.count() == 4
    }

    void "test save"() {
        when:
        assert false, "TODO: Provide a valid instance to save"
        VideoItem videoItem = new VideoItem()
        videoItemService.save(videoItem)

        then:
        videoItem.id != null
    }
}
