package dev.giuseppedarro.comanda.features.orders.data.repository

import dev.giuseppedarro.comanda.features.orders.data.model.SubmitOrderRequest
import dev.giuseppedarro.comanda.features.orders.domain.repository.OrdersRepository

class OrdersRepositoryImpl : OrdersRepository {

    private val orders = mutableListOf<SubmitOrderRequest>()

    override suspend fun submitOrder(request: SubmitOrderRequest): Result<Unit> {
        orders.add(request)
        println("Current orders in memory: $orders")
        return Result.success(Unit)
    }

    override suspend fun getOrders(): List<SubmitOrderRequest> {
        return orders
    }

    override suspend fun getOrdersForTable(tableNumber: Int): List<SubmitOrderRequest> {
        return orders.filter { it.tableNumber == tableNumber }
    }
}
