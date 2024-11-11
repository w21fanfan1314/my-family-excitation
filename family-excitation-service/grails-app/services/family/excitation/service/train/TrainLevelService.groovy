package family.excitation.service.train

import grails.gorm.services.Service

@Service(TrainLevel)
interface TrainLevelService {

    TrainLevel get(Serializable id)

    List<TrainLevel> list(Map args)

    Long count()

    void delete(Serializable id)

    TrainLevel save(TrainLevel trainLevel)

}