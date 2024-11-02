package family.excitation.service

import grails.gorm.services.Service

@Service(AssetStatistics)
interface AssetStatisticsService {

    AssetStatistics get(Serializable id)

    List<AssetStatistics> list(Map args)

    Long count()

    void delete(Serializable id)

    AssetStatistics save(AssetStatistics assetStatistics)

}