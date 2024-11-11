package family.excitation.service.train

import grails.gorm.services.Service

@Service(QuestionOption)
interface QuestionOptionService {

    QuestionOption get(Serializable id)

    List<QuestionOption> list(Map args)

    Long count()

    void delete(Serializable id)

    QuestionOption save(QuestionOption questionOption)

}