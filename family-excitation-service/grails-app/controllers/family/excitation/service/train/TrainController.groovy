package family.excitation.service.train

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class TrainController {

    TrainService trainService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond trainService.list(params), model:[trainCount: trainService.count()]
    }

    def show(Long id) {
        respond trainService.get(id)
    }

    def create() {
        respond new Train(params)
    }

    def save(Train train) {
        if (train == null) {
            notFound()
            return
        }

        try {
            trainService.save(train)
        } catch (ValidationException e) {
            respond train.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'train.label', default: 'Train'), train.id])
                redirect train
            }
            '*' { respond train, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond trainService.get(id)
    }

    def update(Train train) {
        if (train == null) {
            notFound()
            return
        }

        try {
            trainService.save(train)
        } catch (ValidationException e) {
            respond train.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'train.label', default: 'Train'), train.id])
                redirect train
            }
            '*'{ respond train, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        trainService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'train.label', default: 'Train'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'train.label', default: 'Train'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
