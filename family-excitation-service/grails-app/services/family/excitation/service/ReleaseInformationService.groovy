package family.excitation.service

import grails.gorm.services.Service

@Service(ReleaseInformation)
interface ReleaseInformationService {

    ReleaseInformation get(Serializable id)

    List<ReleaseInformation> list(Map args)

    Long count()

    void delete(Serializable id)

    ReleaseInformation save(ReleaseInformation releaseInformation)

}