package family.excitation.service

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class CommodityCategoryController {

    CommodityCategoryService commodityCategoryService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond commodityCategoryService.list(params), model:[commodityCategoryCount: commodityCategoryService.count()]
    }

    def show(Long id) {
        respond commodityCategoryService.get(id)
    }

    def create() {
        respond new CommodityCategory(params)
    }

    def save(CommodityCategory commodityCategory) {
        if (commodityCategory == null) {
            notFound()
            return
        }

        try {
            commodityCategoryService.save(commodityCategory)
        } catch (ValidationException e) {
            respond commodityCategory.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'commodityCategory.label', default: 'CommodityCategory'), commodityCategory.id])
                redirect commodityCategory
            }
            '*' { respond commodityCategory, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond commodityCategoryService.get(id)
    }

    def update(CommodityCategory commodityCategory) {
        if (commodityCategory == null) {
            notFound()
            return
        }

        try {
            commodityCategoryService.save(commodityCategory)
        } catch (ValidationException e) {
            respond commodityCategory.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'commodityCategory.label', default: 'CommodityCategory'), commodityCategory.id])
                redirect commodityCategory
            }
            '*'{ respond commodityCategory, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        commodityCategoryService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'commodityCategory.label', default: 'CommodityCategory'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'commodityCategory.label', default: 'CommodityCategory'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
