package family.excitation.service

import grails.gorm.services.Service

@Service(UserRecord)
interface UserRecordService {

    UserRecord get(Serializable id)

    List<UserRecord> list(Map args)

    Long count()

    void delete(Serializable id)

    UserRecord save(UserRecord userRecord)

}