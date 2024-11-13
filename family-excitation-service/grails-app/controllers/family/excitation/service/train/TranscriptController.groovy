package family.excitation.service.train

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class TranscriptController {

    TranscriptService transcriptService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond transcriptService.list(params), model:[transcriptCount: transcriptService.count()]
    }

    def show(Long id) {
        respond transcriptService.get(id)
    }

    def create() {
        respond new Transcript(params)
    }

    def save(Transcript transcript) {
        if (transcript == null) {
            notFound()
            return
        }

        try {
            transcriptService.save(transcript)
        } catch (ValidationException e) {
            respond transcript.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'transcript.label', default: 'Transcript'), transcript.id])
                redirect transcript
            }
            '*' { respond transcript, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond transcriptService.get(id)
    }

    def update(Transcript transcript) {
        if (transcript == null) {
            notFound()
            return
        }

        try {
            transcriptService.save(transcript)
        } catch (ValidationException e) {
            respond transcript.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'transcript.label', default: 'Transcript'), transcript.id])
                redirect transcript
            }
            '*'{ respond transcript, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        transcriptService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'transcript.label', default: 'Transcript'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'transcript.label', default: 'Transcript'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
