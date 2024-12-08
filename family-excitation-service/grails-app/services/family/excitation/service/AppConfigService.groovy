package family.excitation.service

import grails.gorm.services.Service

@Service(AppConfig)
interface AppConfigService {

    AppConfig get(Serializable id)

    List<AppConfig> list(Map args)

    Long count()

    void delete(Serializable id)

    AppConfig save(AppConfig appConfig)

}