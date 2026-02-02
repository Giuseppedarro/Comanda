package dev.giuseppedarro.comanda.features.orders.domain.repository

import dev.giuseppedarro.comanda.core.utils.Result
import dev.giuseppedarro.comanda.features.orders.domain.model.MenuCategory
import dev.giuseppedarro.comanda.features.orders.domain.model.Order
import dev.giuseppedarro.comanda.features.orders.domain.model.OrderItem
import kotlinx.coroutines.flow.Flow

interface OrderRepository {

    /**
     * Fetches the full menu (all categories and items) from the data source.
     */
    fun getMenu(): Flow<Result<List<MenuCategory>>>

    /**
     * Fetches the existing order for a specific table.
     * Returns null if no order exists for the table.
     */
    fun getOrdersForTable(tableNumber: Int): Flow<Result<Order?>>

    /**
     * Submits a completed order to the backend.
     */
    suspend fun submitOrder(tableNumber: Int, numberOfPeople: Int, items: List<OrderItem>): Result<Unit>

    /**
     * Submits the order and prints the bill.
     */
    suspend fun printBill(tableNumber: Int, numberOfPeople: Int, items: List<OrderItem>): Result<Unit>
}