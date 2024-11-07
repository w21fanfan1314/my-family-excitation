package family.excitation.service

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class MediaDataController {

    MediaDataService mediaDataService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond mediaDataService.list(params), model:[mediaDataCount: mediaDataService.count()]
    }

    def show(Long id) {
        respond mediaDataService.get(id)
    }

    def create() {
        respond new MediaData(params)
    }

    def save(MediaData mediaData) {
        if (mediaData == null) {
            notFound()
            return
        }

        try {
            mediaDataService.save(mediaData)
        } catch (ValidationException e) {
            respond mediaData.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'mediaData.label', default: 'MediaData'), mediaData.id])
                redirect mediaData
            }
            '*' { respond mediaData, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond mediaDataService.get(id)
    }

    def update(MediaData mediaData) {
        if (mediaData == null) {
            notFound()
            return
        }

        try {
            mediaDataService.save(mediaData)
        } catch (ValidationException e) {
            respond mediaData.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'mediaData.label', default: 'MediaData'), mediaData.id])
                redirect mediaData
            }
            '*'{ respond mediaData, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        mediaDataService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'mediaData.label', default: 'MediaData'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'mediaData.label', default: 'MediaData'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
