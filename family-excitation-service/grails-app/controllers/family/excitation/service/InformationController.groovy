package family.excitation.service

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class InformationController {

    InformationService informationService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond informationService.list(params), model:[informationCount: informationService.count()]
    }

    def show(Long id) {
        respond informationService.get(id)
    }

    def create() {
        respond new Information(params)
    }

    def save(Information information) {
        if (information == null) {
            notFound()
            return
        }

        try {
            informationService.save(information)
        } catch (ValidationException e) {
            respond information.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'information.label', default: 'Information'), information.id])
                redirect information
            }
            '*' { respond information, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond informationService.get(id)
    }

    def update(Information information) {
        if (information == null) {
            notFound()
            return
        }

        try {
            informationService.save(information)
        } catch (ValidationException e) {
            respond information.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'information.label', default: 'Information'), information.id])
                redirect information
            }
            '*'{ respond information, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        informationService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'information.label', default: 'Information'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'information.label', default: 'Information'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
