package family.excitation.service.train

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class UserAnswerController {

    UserAnswerService userAnswerService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond userAnswerService.list(params), model:[userAnswerCount: userAnswerService.count()]
    }

    def show(Long id) {
        respond userAnswerService.get(id)
    }

    def create() {
        respond new UserAnswer(params)
    }

    def save(UserAnswer userAnswer) {
        if (userAnswer == null) {
            notFound()
            return
        }

        try {
            userAnswerService.save(userAnswer)
        } catch (ValidationException e) {
            respond userAnswer.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'userAnswer.label', default: 'UserAnswer'), userAnswer.id])
                redirect userAnswer
            }
            '*' { respond userAnswer, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond userAnswerService.get(id)
    }

    def update(UserAnswer userAnswer) {
        if (userAnswer == null) {
            notFound()
            return
        }

        try {
            userAnswerService.save(userAnswer)
        } catch (ValidationException e) {
            respond userAnswer.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'userAnswer.label', default: 'UserAnswer'), userAnswer.id])
                redirect userAnswer
            }
            '*'{ respond userAnswer, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        userAnswerService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'userAnswer.label', default: 'UserAnswer'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'userAnswer.label', default: 'UserAnswer'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
