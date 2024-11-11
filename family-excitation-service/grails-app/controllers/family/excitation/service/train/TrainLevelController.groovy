package family.excitation.service.train

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class TrainLevelController {

    TrainLevelService trainLevelService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond trainLevelService.list(params), model:[trainLevelCount: trainLevelService.count()]
    }

    def show(Long id) {
        respond trainLevelService.get(id)
    }

    def create() {
        respond new TrainLevel(params)
    }

    def save(TrainLevel trainLevel) {
        if (trainLevel == null) {
            notFound()
            return
        }

        try {
            trainLevelService.save(trainLevel)
        } catch (ValidationException e) {
            respond trainLevel.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'trainLevel.label', default: 'TrainLevel'), trainLevel.id])
                redirect trainLevel
            }
            '*' { respond trainLevel, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond trainLevelService.get(id)
    }

    def update(TrainLevel trainLevel) {
        if (trainLevel == null) {
            notFound()
            return
        }

        try {
            trainLevelService.save(trainLevel)
        } catch (ValidationException e) {
            respond trainLevel.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'trainLevel.label', default: 'TrainLevel'), trainLevel.id])
                redirect trainLevel
            }
            '*'{ respond trainLevel, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        trainLevelService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'trainLevel.label', default: 'TrainLevel'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'trainLevel.label', default: 'TrainLevel'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
