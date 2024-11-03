package family.excitation.service

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class OrderPaymentController {

    OrderPaymentService orderPaymentService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond orderPaymentService.list(params), model:[orderPaymentCount: orderPaymentService.count()]
    }

    def show(Long id) {
        respond orderPaymentService.get(id)
    }

    def create() {
        respond new OrderPayment(params)
    }

    def save(OrderPayment orderPayment) {
        if (orderPayment == null) {
            notFound()
            return
        }

        try {
            orderPaymentService.save(orderPayment)
        } catch (ValidationException e) {
            respond orderPayment.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'orderPayment.label', default: 'OrderPayment'), orderPayment.id])
                redirect orderPayment
            }
            '*' { respond orderPayment, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond orderPaymentService.get(id)
    }

    def update(OrderPayment orderPayment) {
        if (orderPayment == null) {
            notFound()
            return
        }

        try {
            orderPaymentService.save(orderPayment)
        } catch (ValidationException e) {
            respond orderPayment.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'orderPayment.label', default: 'OrderPayment'), orderPayment.id])
                redirect orderPayment
            }
            '*'{ respond orderPayment, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        orderPaymentService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'orderPayment.label', default: 'OrderPayment'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'orderPayment.label', default: 'OrderPayment'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
