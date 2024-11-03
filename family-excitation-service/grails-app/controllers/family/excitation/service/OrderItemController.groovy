package family.excitation.service

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class OrderItemController {

    OrderItemService orderItemService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond orderItemService.list(params), model:[orderItemCount: orderItemService.count()]
    }

    def show(Long id) {
        respond orderItemService.get(id)
    }

    def create() {
        respond new OrderItem(params)
    }

    def save(OrderItem orderItem) {
        if (orderItem == null) {
            notFound()
            return
        }

        try {
            orderItemService.save(orderItem)
        } catch (ValidationException e) {
            respond orderItem.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'orderItem.label', default: 'OrderItem'), orderItem.id])
                redirect orderItem
            }
            '*' { respond orderItem, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond orderItemService.get(id)
    }

    def update(OrderItem orderItem) {
        if (orderItem == null) {
            notFound()
            return
        }

        try {
            orderItemService.save(orderItem)
        } catch (ValidationException e) {
            respond orderItem.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'orderItem.label', default: 'OrderItem'), orderItem.id])
                redirect orderItem
            }
            '*'{ respond orderItem, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        orderItemService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'orderItem.label', default: 'OrderItem'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'orderItem.label', default: 'OrderItem'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
