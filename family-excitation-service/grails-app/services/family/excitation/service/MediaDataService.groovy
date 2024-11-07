package family.excitation.service

import grails.gorm.services.Service

@Service(MediaData)
interface MediaDataService {

    MediaData get(Serializable id)

    List<MediaData> list(Map args)

    Long count()

    void delete(Serializable id)

    MediaData save(MediaData mediaData)

}