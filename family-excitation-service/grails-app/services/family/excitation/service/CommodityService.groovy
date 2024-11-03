package family.excitation.service

import grails.gorm.services.Service

@Service(Commodity)
interface CommodityService {

    Commodity get(Serializable id)

    List<Commodity> list(Map args)

    Long count()

    void delete(Serializable id)

    Commodity save(Commodity commodity)

}