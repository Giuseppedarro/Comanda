package dev.giuseppedarro.comanda.features.orders.data.repository

import dev.giuseppedarro.comanda.features.orders.data.model.SubmitOrderRequest
import dev.giuseppedarro.comanda.features.orders.domain.repository.OrdersRepository

class OrdersRepositoryImpl : OrdersRepository {
    override suspend fun submitOrder(request: SubmitOrderRequest): Result<Unit> {
        // In the future, this will insert the order into the database.
        println("Received order for table ${request.tableNumber} with ${request.numberOfPeople} people: ${request.items}")
        return Result.success(Unit)
    }
}
