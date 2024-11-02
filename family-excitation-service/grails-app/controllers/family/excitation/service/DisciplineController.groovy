package family.excitation.service

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class DisciplineController {

    DisciplineService disciplineService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond disciplineService.list(params), model:[disciplineCount: disciplineService.count()]
    }

    def show(Long id) {
        respond disciplineService.get(id)
    }

    def create() {
        respond new Discipline(params)
    }

    def save(Discipline discipline) {
        if (discipline == null) {
            notFound()
            return
        }

        try {
            disciplineService.save(discipline)
        } catch (ValidationException e) {
            respond discipline.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'discipline.label', default: 'Discipline'), discipline.id])
                redirect discipline
            }
            '*' { respond discipline, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond disciplineService.get(id)
    }

    def update(Discipline discipline) {
        if (discipline == null) {
            notFound()
            return
        }

        try {
            disciplineService.save(discipline)
        } catch (ValidationException e) {
            respond discipline.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'discipline.label', default: 'Discipline'), discipline.id])
                redirect discipline
            }
            '*'{ respond discipline, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        disciplineService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'discipline.label', default: 'Discipline'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'discipline.label', default: 'Discipline'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
