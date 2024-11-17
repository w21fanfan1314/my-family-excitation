package family.excitation.service

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class TestPaperTrackController {

    TestPaperTrackService testPaperTrackService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond testPaperTrackService.list(params), model:[testPaperTrackCount: testPaperTrackService.count()]
    }

    def show(Long id) {
        respond testPaperTrackService.get(id)
    }

    def create() {
        respond new TestPaperTrack(params)
    }

    def save(TestPaperTrack testPaperTrack) {
        if (testPaperTrack == null) {
            notFound()
            return
        }

        try {
            testPaperTrackService.save(testPaperTrack)
        } catch (ValidationException e) {
            respond testPaperTrack.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'testPaperTrack.label', default: 'TestPaperTrack'), testPaperTrack.id])
                redirect testPaperTrack
            }
            '*' { respond testPaperTrack, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond testPaperTrackService.get(id)
    }

    def update(TestPaperTrack testPaperTrack) {
        if (testPaperTrack == null) {
            notFound()
            return
        }

        try {
            testPaperTrackService.save(testPaperTrack)
        } catch (ValidationException e) {
            respond testPaperTrack.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'testPaperTrack.label', default: 'TestPaperTrack'), testPaperTrack.id])
                redirect testPaperTrack
            }
            '*'{ respond testPaperTrack, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        testPaperTrackService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'testPaperTrack.label', default: 'TestPaperTrack'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'testPaperTrack.label', default: 'TestPaperTrack'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
