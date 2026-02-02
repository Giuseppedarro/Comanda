package dev.giuseppedarro.comanda.features.orders.domain.repository

import dev.giuseppedarro.comanda.features.orders.data.model.SubmitOrderRequest
import dev.giuseppedarro.comanda.features.orders.domain.model.Order

interface OrdersRepository {
    suspend fun submitOrder(request: SubmitOrderRequest): Result<Order>
    suspend fun getOrders(): List<Order>
    suspend fun getOrdersForTable(tableNumber: Int): List<Order>
    suspend fun getOrderById(orderId: String): Order?
    suspend fun deleteOrder(tableNumber: Int)
}
