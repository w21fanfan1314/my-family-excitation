package family.excitation.service

import grails.gorm.services.Service

@Service(Discipline)
interface DisciplineService {

    Discipline get(Serializable id)

    List<Discipline> list(Map args)

    Long count()

    void delete(Serializable id)

    Discipline save(Discipline discipline)

}