package family.excitation.service.train

import grails.gorm.services.Service

@Service(UserAnswer)
interface UserAnswerService {

    UserAnswer get(Serializable id)

    List<UserAnswer> list(Map args)

    Long count()

    void delete(Serializable id)

    UserAnswer save(UserAnswer userAnswer)

}