package family.excitation.service

import grails.gorm.services.Service

@Service(Lottery)
interface LotteryService {

    Lottery get(Serializable id)

    List<Lottery> list(Map args)

    Long count()

    void delete(Serializable id)

    Lottery save(Lottery lottery)

}