package dev.giuseppedarro.comanda.features.payments.domain.usecase

import dev.giuseppedarro.comanda.features.orders.data.Orders
import dev.giuseppedarro.comanda.features.orders.domain.model.OrderStatus
import dev.giuseppedarro.comanda.features.payments.data.model.PaymentRequest
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.transactions.transaction

class ProcessPaymentUseCase {
    suspend operator fun invoke(request: PaymentRequest): Result<Unit> {
        return try {
            transaction {
                val updated = Orders.update({ Orders.id eq request.orderId }) {
                    it[status] = OrderStatus.closed.name
                    it[total] = request.amount
                }
                
                if (updated > 0) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Order not found"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
