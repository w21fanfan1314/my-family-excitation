package family.excitation.service

import grails.gorm.services.Service

@Service(OrderPayment)
interface OrderPaymentService {

    OrderPayment get(Serializable id)

    List<OrderPayment> list(Map args)

    Long count()

    void delete(Serializable id)

    OrderPayment save(OrderPayment orderPayment)

}