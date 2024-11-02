package family.excitation.service

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class CurrencyController {

    CurrencyService currencyService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond currencyService.list(params), model:[currencyCount: currencyService.count()]
    }

    def show(Long id) {
        respond currencyService.get(id)
    }

    def create() {
        respond new Currency(params)
    }

    def save(Currency currency) {
        if (currency == null) {
            notFound()
            return
        }

        try {
            currencyService.save(currency)
        } catch (ValidationException e) {
            respond currency.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'currency.label', default: 'Currency'), currency.id])
                redirect currency
            }
            '*' { respond currency, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond currencyService.get(id)
    }

    def update(Currency currency) {
        if (currency == null) {
            notFound()
            return
        }

        try {
            currencyService.save(currency)
        } catch (ValidationException e) {
            respond currency.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'currency.label', default: 'Currency'), currency.id])
                redirect currency
            }
            '*'{ respond currency, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        currencyService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'currency.label', default: 'Currency'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'currency.label', default: 'Currency'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
