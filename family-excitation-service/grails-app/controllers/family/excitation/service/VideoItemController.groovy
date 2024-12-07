package family.excitation.service

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class VideoItemController {

    VideoItemService videoItemService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond videoItemService.list(params), model:[videoItemCount: videoItemService.count()]
    }

    def show(Long id) {
        respond videoItemService.get(id)
    }

    def create() {
        respond new VideoItem(params)
    }

    def save(VideoItem videoItem) {
        if (videoItem == null) {
            notFound()
            return
        }

        try {
            videoItemService.save(videoItem)
        } catch (ValidationException e) {
            respond videoItem.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'videoItem.label', default: 'VideoItem'), videoItem.id])
                redirect videoItem
            }
            '*' { respond videoItem, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond videoItemService.get(id)
    }

    def update(VideoItem videoItem) {
        if (videoItem == null) {
            notFound()
            return
        }

        try {
            videoItemService.save(videoItem)
        } catch (ValidationException e) {
            respond videoItem.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'videoItem.label', default: 'VideoItem'), videoItem.id])
                redirect videoItem
            }
            '*'{ respond videoItem, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        videoItemService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'videoItem.label', default: 'VideoItem'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'videoItem.label', default: 'VideoItem'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
