package family.excitation.service.train

import grails.gorm.services.Service

@Service(Train)
interface TrainService {

    Train get(Serializable id)

    List<Train> list(Map args)

    Long count()

    void delete(Serializable id)

    Train save(Train train)

}