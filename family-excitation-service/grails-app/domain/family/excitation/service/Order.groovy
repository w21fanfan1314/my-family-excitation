package family.excitation.service

import org.apache.commons.lang3.RandomUtils

import java.text.SimpleDateFormat

class Order {
    String orderNo
    Double totalPrice = 0
    OrderStatus status = OrderStatus.WAIT_PAY
    Date dateCreated
    Date lastUpdated

    static belongsTo = [user: User]
    static hasMany = [items: OrderItem, payments: OrderPayment]
    static constraints = {
        orderNo nullable: true, unique: true
        totalPrice min: 0d
        payments nullable: true
    }

    static mapping = {
        sort 'dateCreated'
        order 'desc'
        table 'user_order'
    }

    def beforeInsert() {
        if (!orderNo) {
            SimpleDateFormat orderNoGenerator = new SimpleDateFormat("yyyyMMddHHmmssSSS")
            orderNo = orderNoGenerator.format(new Date()).concat(RandomUtils.nextLong(1, 1000).toString())
        }
        if (totalPrice == 0) {
            totalPrice = items?.sum { it.price * it.buyCount } ?: 0d
        }

        if (totalPrice == 0) {
            status = OrderStatus.PAID
        }
    }

    def afterUpdate() {
        if (status == OrderStatus.WAIT_PAY) {
            if (this.payed) {
                status = OrderStatus.PAID
                this.save()
            }
        }
    }

    boolean isPayed() {
        if (status != OrderStatus.PAID) {
            def payAmount = payments.sum { it.amount }
            return totalPrice == payAmount
        }
        return true
    }

    @Override
    String toString() {
        return orderNo
    }
}

enum OrderStatus {
    // 待支付
    WAIT_PAY,
    // 已支付
    PAID,
    // 已发货
    SHIPPED,
    // 已完成
    COMPLETED,
    // 已取消
    CANCELLED
}

