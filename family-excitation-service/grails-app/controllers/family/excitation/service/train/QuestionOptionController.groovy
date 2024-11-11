package family.excitation.service.train

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class QuestionOptionController {

    QuestionOptionService questionOptionService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond questionOptionService.list(params), model:[questionOptionCount: questionOptionService.count()]
    }

    def show(Long id) {
        respond questionOptionService.get(id)
    }

    def create() {
        respond new QuestionOption(params)
    }

    def save(QuestionOption questionOption) {
        if (questionOption == null) {
            notFound()
            return
        }

        try {
            questionOptionService.save(questionOption)
        } catch (ValidationException e) {
            respond questionOption.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'questionOption.label', default: 'QuestionOption'), questionOption.id])
                redirect questionOption
            }
            '*' { respond questionOption, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond questionOptionService.get(id)
    }

    def update(QuestionOption questionOption) {
        if (questionOption == null) {
            notFound()
            return
        }

        try {
            questionOptionService.save(questionOption)
        } catch (ValidationException e) {
            respond questionOption.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'questionOption.label', default: 'QuestionOption'), questionOption.id])
                redirect questionOption
            }
            '*'{ respond questionOption, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        questionOptionService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'questionOption.label', default: 'QuestionOption'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'questionOption.label', default: 'QuestionOption'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
