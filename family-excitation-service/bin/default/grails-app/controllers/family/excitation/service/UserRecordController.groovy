package family.excitation.service

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class UserRecordController {

    UserRecordService userRecordService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond userRecordService.list(params), model:[userRecordCount: userRecordService.count()]
    }

    def show(Long id) {
        respond userRecordService.get(id)
    }

    def create() {
        respond new UserRecord(params)
    }

    def save(UserRecord userRecord) {
        if (userRecord == null) {
            notFound()
            return
        }

        try {
            userRecordService.save(userRecord)
        } catch (ValidationException e) {
            respond userRecord.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'userRecord.label', default: 'UserRecord'), userRecord.id])
                redirect userRecord
            }
            '*' { respond userRecord, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond userRecordService.get(id)
    }

    def update(UserRecord userRecord) {
        if (userRecord == null) {
            notFound()
            return
        }

        try {
            userRecordService.save(userRecord)
        } catch (ValidationException e) {
            respond userRecord.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'userRecord.label', default: 'UserRecord'), userRecord.id])
                redirect userRecord
            }
            '*'{ respond userRecord, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        userRecordService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'userRecord.label', default: 'UserRecord'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'userRecord.label', default: 'UserRecord'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
