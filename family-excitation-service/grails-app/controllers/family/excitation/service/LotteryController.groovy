package family.excitation.service

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class LotteryController {

    LotteryService lotteryService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond lotteryService.list(params), model:[lotteryCount: lotteryService.count()]
    }

    def show(Long id) {
        respond lotteryService.get(id)
    }

    def create() {
        respond new Lottery(params)
    }

    def save(Lottery lottery) {
        if (lottery == null) {
            notFound()
            return
        }

        try {
            lotteryService.save(lottery)
        } catch (ValidationException e) {
            respond lottery.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'lottery.label', default: 'Lottery'), lottery.id])
                redirect lottery
            }
            '*' { respond lottery, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond lotteryService.get(id)
    }

    def update(Lottery lottery) {
        if (lottery == null) {
            notFound()
            return
        }

        try {
            lotteryService.save(lottery)
        } catch (ValidationException e) {
            respond lottery.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'lottery.label', default: 'Lottery'), lottery.id])
                redirect lottery
            }
            '*'{ respond lottery, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        lotteryService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'lottery.label', default: 'Lottery'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'lottery.label', default: 'Lottery'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
