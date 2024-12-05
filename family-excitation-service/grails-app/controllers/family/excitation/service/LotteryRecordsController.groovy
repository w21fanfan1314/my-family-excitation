package family.excitation.service

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class LotteryRecordsController {

    LotteryRecordsService lotteryRecordsService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond lotteryRecordsService.list(params), model:[lotteryRecordsCount: lotteryRecordsService.count()]
    }

    def show(Long id) {
        respond lotteryRecordsService.get(id)
    }

    def create() {
        respond new LotteryRecords(params)
    }

    def save(LotteryRecords lotteryRecords) {
        if (lotteryRecords == null) {
            notFound()
            return
        }

        try {
            lotteryRecordsService.save(lotteryRecords)
        } catch (ValidationException e) {
            respond lotteryRecords.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'lotteryRecords.label', default: 'LotteryRecords'), lotteryRecords.id])
                redirect lotteryRecords
            }
            '*' { respond lotteryRecords, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond lotteryRecordsService.get(id)
    }

    def update(LotteryRecords lotteryRecords) {
        if (lotteryRecords == null) {
            notFound()
            return
        }

        try {
            lotteryRecordsService.save(lotteryRecords)
        } catch (ValidationException e) {
            respond lotteryRecords.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'lotteryRecords.label', default: 'LotteryRecords'), lotteryRecords.id])
                redirect lotteryRecords
            }
            '*'{ respond lotteryRecords, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        lotteryRecordsService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'lotteryRecords.label', default: 'LotteryRecords'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'lotteryRecords.label', default: 'LotteryRecords'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
