package family.excitation.service

class OrderPayment {
    Double amount = 0
    Currency currency
    Date dateCreated
    Date lastUpdated

    static belongsTo = [order: Order]
    static constraints = {
        amount min: 0d
    }

    def afterInsert() {
        if (order.status != OrderStatus.PAID && order.isPayed()) {
            order.status = OrderStatus.PAID
            order.save()
        }

        UserRecord userRecord = new UserRecord(user: order.user, recordType: UserRecordType.CONSUME, currency: currency, amount: -(amount), content: "购买了:\n " +
                "${(order.items?.collect {"${it.name} ${it.price} x${it.buyCount}"}).join("\n")}")
        userRecord.save()
    }


    @Override
    String toString() {
        return amount
    }
}
