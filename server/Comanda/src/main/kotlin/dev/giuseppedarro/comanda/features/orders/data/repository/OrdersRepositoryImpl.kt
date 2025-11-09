package dev.giuseppedarro.comanda.features.orders.data.repository

import dev.giuseppedarro.comanda.features.orders.data.model.SubmitOrderRequest
import dev.giuseppedarro.comanda.features.orders.domain.model.Order
import dev.giuseppedarro.comanda.features.orders.domain.model.OrderItem
import dev.giuseppedarro.comanda.features.orders.domain.model.OrderStatus
import dev.giuseppedarro.comanda.features.orders.domain.repository.OrdersRepository
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.UUID

class OrdersRepositoryImpl : OrdersRepository {

    private val orders = mutableListOf<Order>()

    override suspend fun submitOrder(request: SubmitOrderRequest): Result<Order> {
        val orderId = UUID.randomUUID().toString()
        var itemCounter = 0
        val items = request.items.map {
            itemCounter += 1
            OrderItem(
                orderItemId = "oi_${itemCounter}",
                itemId = it.menuItemId,
                quantity = it.quantity,
                notes = it.notes
            )
        }
        val created = Order(
            id = orderId,
            tableNumber = request.tableNumber,
            numberOfPeople = request.numberOfPeople,
            status = OrderStatus.open,
            items = items,
            createdAt = DateTimeFormatter.ISO_INSTANT.format(Instant.now()),
            subtotal = null,
            serviceCharge = null,
            total = null
        )
        orders.add(created)
        println("Current orders in memory: $orders")
        return Result.success(created)
    }

    override suspend fun getOrders(): List<Order> {
        return orders
    }

    override suspend fun getOrdersForTable(tableNumber: Int): List<Order> {
        return orders.filter { it.tableNumber == tableNumber }
    }

    override suspend fun getOrderById(orderId: String): Order? {
        return orders.find { it.id == orderId }
    }
}
