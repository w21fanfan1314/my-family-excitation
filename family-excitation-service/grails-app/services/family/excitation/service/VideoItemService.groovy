package family.excitation.service

import grails.gorm.services.Service

@Service(VideoItem)
interface VideoItemService {

    VideoItem get(Serializable id)

    List<VideoItem> list(Map args)

    Long count()

    void delete(Serializable id)

    VideoItem save(VideoItem videoItem)

}