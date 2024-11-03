package family.excitation.service

import grails.gorm.services.Service

@Service(Information)
interface InformationService {

    Information get(Serializable id)

    List<Information> list(Map args)

    Long count()

    void delete(Serializable id)

    Information save(Information information)

}