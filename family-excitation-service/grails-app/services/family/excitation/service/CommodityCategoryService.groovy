package family.excitation.service

import grails.gorm.services.Service

@Service(CommodityCategory)
interface CommodityCategoryService {

    CommodityCategory get(Serializable id)

    List<CommodityCategory> list(Map args)

    Long count()

    void delete(Serializable id)

    CommodityCategory save(CommodityCategory commodityCategory)

}