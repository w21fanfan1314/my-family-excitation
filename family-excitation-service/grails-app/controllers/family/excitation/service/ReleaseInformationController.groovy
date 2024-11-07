package family.excitation.service

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class ReleaseInformationController {

    ReleaseInformationService releaseInformationService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond releaseInformationService.list(params), model:[releaseInformationCount: releaseInformationService.count()]
    }

    def show(Long id) {
        respond releaseInformationService.get(id)
    }

    def create() {
        respond new ReleaseInformation(params)
    }

    def save(ReleaseInformation releaseInformation) {
        if (releaseInformation == null) {
            notFound()
            return
        }

        try {
            releaseInformationService.save(releaseInformation)
        } catch (ValidationException e) {
            respond releaseInformation.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'releaseInformation.label', default: 'ReleaseInformation'), releaseInformation.id])
                redirect releaseInformation
            }
            '*' { respond releaseInformation, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond releaseInformationService.get(id)
    }

    def update(ReleaseInformation releaseInformation) {
        if (releaseInformation == null) {
            notFound()
            return
        }

        try {
            releaseInformationService.save(releaseInformation)
        } catch (ValidationException e) {
            respond releaseInformation.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'releaseInformation.label', default: 'ReleaseInformation'), releaseInformation.id])
                redirect releaseInformation
            }
            '*'{ respond releaseInformation, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        releaseInformationService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'releaseInformation.label', default: 'ReleaseInformation'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'releaseInformation.label', default: 'ReleaseInformation'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
