package family.excitation.service

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class AppConfigController {

    AppConfigService appConfigService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond appConfigService.list(params), model:[appConfigCount: appConfigService.count()]
    }

    def show(Long id) {
        respond appConfigService.get(id)
    }

    def create() {
        respond new AppConfig(params)
    }

    def save(AppConfig appConfig) {
        if (appConfig == null) {
            notFound()
            return
        }

        try {
            appConfigService.save(appConfig)
        } catch (ValidationException e) {
            respond appConfig.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'appConfig.label', default: 'AppConfig'), appConfig.id])
                redirect appConfig
            }
            '*' { respond appConfig, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond appConfigService.get(id)
    }

    def update(AppConfig appConfig) {
        if (appConfig == null) {
            notFound()
            return
        }

        try {
            appConfigService.save(appConfig)
        } catch (ValidationException e) {
            respond appConfig.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'appConfig.label', default: 'AppConfig'), appConfig.id])
                redirect appConfig
            }
            '*'{ respond appConfig, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        appConfigService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'appConfig.label', default: 'AppConfig'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'appConfig.label', default: 'AppConfig'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
