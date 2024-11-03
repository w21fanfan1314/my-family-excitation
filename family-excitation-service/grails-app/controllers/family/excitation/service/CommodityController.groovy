package family.excitation.service

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class CommodityController {

    CommodityService commodityService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond commodityService.list(params), model:[commodityCount: commodityService.count()]
    }

    def show(Long id) {
        respond commodityService.get(id)
    }

    def create() {
        respond new Commodity(params)
    }

    def save(Commodity commodity) {
        if (commodity == null) {
            notFound()
            return
        }

        try {
            commodityService.save(commodity)
        } catch (ValidationException e) {
            respond commodity.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'commodity.label', default: 'Commodity'), commodity.id])
                redirect commodity
            }
            '*' { respond commodity, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond commodityService.get(id)
    }

    def update(Commodity commodity) {
        if (commodity == null) {
            notFound()
            return
        }

        try {
            commodityService.save(commodity)
        } catch (ValidationException e) {
            respond commodity.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'commodity.label', default: 'Commodity'), commodity.id])
                redirect commodity
            }
            '*'{ respond commodity, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        commodityService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'commodity.label', default: 'Commodity'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'commodity.label', default: 'Commodity'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
