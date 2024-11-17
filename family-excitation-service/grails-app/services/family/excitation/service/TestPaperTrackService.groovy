package family.excitation.service

import grails.gorm.services.Service

@Service(TestPaperTrack)
interface TestPaperTrackService {

    TestPaperTrack get(Serializable id)

    List<TestPaperTrack> list(Map args)

    Long count()

    void delete(Serializable id)

    TestPaperTrack save(TestPaperTrack testPaperTrack)

}