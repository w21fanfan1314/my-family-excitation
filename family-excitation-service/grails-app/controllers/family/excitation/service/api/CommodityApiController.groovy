package family.excitation.service.api

import family.excitation.service.Commodity
import family.excitation.service.CommodityCategory
import family.excitation.service.Currency

class CommodityApiController {

    def list(Integer page,  Integer size, CommodityCategory category, String searchKey) {
        def c = Commodity.createCriteria()
        def commodities = c.list {
            if (category) {
                eq('category', category)
            }
            if (searchKey) {
                like('name', "%${searchKey}%")
            }
            order('dateCreated', 'desc')
            maxResults(size)
            firstResult(page * size)
        }
        c = Commodity.createCriteria()
        def rowCount = c.get {
            if (category) {
                eq('category', category)
            }
            if (searchKey) {
                like('name', "%${searchKey}%")
            }
            projections {
                rowCount()
            }
        }
        respond new ApiResult(code: 200, msg: '查询成功', data: [commodities: commodities, total: rowCount])
    }

    def listCategories() {
        respond new ApiResult(code: 200, msg: '查询成功', data: CommodityCategory.list())
    }

    def listCurrencies() {
        respond new ApiResult(code: 200, msg: '查询成功', data: Currency.list())
    }
}
