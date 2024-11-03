package family.excitation.service.api

import family.excitation.service.Login
import family.excitation.service.Order
import family.excitation.service.OrderService
import family.excitation.service.OrderStatus
import family.excitation.service.User
import grails.converters.JSON

class OrderApiController {
    static allowedMethods = [createOrder: "POST"]
    OrderService orderService

    def createOrder() {
        User user = Login.findByToken(request.getHeader('app-token'))?.user
        def json = request.getJSON()
        Order order = new Order(user: user, items: json.items, payments: json.payments)
        if (order.validate()) {
            Order result = orderService.save(order)
            JSON.use('deep') {
                respond new ApiResult(code: 200,  msg: '订单创建成功', data: result)
            }
        } else {
            respond new ApiResult(code: 500,  msg: '订单创建失败', data: order.errors)
        }
    }


    def listOrders(Integer page = 0, Integer size = 20, OrderStatus status) {
        User user = Login.findByToken(request.getHeader('app-token'))?.user
        if (!user) {
            respond new ApiResult(code: 500, msg: '用户不存在')
            return
        }
        def orders = Order.createCriteria().list {
            eq('user', user)
            if (status) {
                eq('status', status)
            }
            maxResults(size)
            firstResult(page * size)
            order('dateCreated', 'desc')
        }

        def rowCount = Order.createCriteria().get {
            eq('user', user)
            if (status) {
                eq('status', status)
            }
            projections {
                rowCount()
            }
        }
        JSON.use('deep') {
            respond new ApiResult(code: 200, msg: '获取成功', data: [orders: orders, total:rowCount])
        }
    }

}
