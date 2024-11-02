package family.excitation.service

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class AssetStatisticsController {

    AssetStatisticsService assetStatisticsService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond assetStatisticsService.list(params), model:[assetStatisticsCount: assetStatisticsService.count()]
    }

    def show(Long id) {
        respond assetStatisticsService.get(id)
    }

    def create() {
        respond new AssetStatistics(params)
    }

    def save(AssetStatistics assetStatistics) {
        if (assetStatistics == null) {
            notFound()
            return
        }

        try {
            assetStatisticsService.save(assetStatistics)
        } catch (ValidationException e) {
            respond assetStatistics.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'assetStatistics.label', default: 'AssetStatistics'), assetStatistics.id])
                redirect assetStatistics
            }
            '*' { respond assetStatistics, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond assetStatisticsService.get(id)
    }

    def update(AssetStatistics assetStatistics) {
        if (assetStatistics == null) {
            notFound()
            return
        }

        try {
            assetStatisticsService.save(assetStatistics)
        } catch (ValidationException e) {
            respond assetStatistics.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'assetStatistics.label', default: 'AssetStatistics'), assetStatistics.id])
                redirect assetStatistics
            }
            '*'{ respond assetStatistics, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        assetStatisticsService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'assetStatistics.label', default: 'AssetStatistics'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'assetStatistics.label', default: 'AssetStatistics'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
