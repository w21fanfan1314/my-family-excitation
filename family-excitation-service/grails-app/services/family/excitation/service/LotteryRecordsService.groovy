package family.excitation.service

import grails.gorm.services.Service

@Service(LotteryRecords)
interface LotteryRecordsService {

    LotteryRecords get(Serializable id)

    List<LotteryRecords> list(Map args)

    Long count()

    void delete(Serializable id)

    LotteryRecords save(LotteryRecords lotteryRecords)

}