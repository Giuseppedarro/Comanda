package dev.giuseppedarro.comanda.features.orders.domain.repository

import dev.giuseppedarro.comanda.features.orders.data.model.SubmitOrderRequest

interface OrdersRepository {
    suspend fun submitOrder(request: SubmitOrderRequest): Result<Unit>
    suspend fun getOrders(): List<SubmitOrderRequest>
    suspend fun getOrdersForTable(tableNumber: Int): List<SubmitOrderRequest>
}
