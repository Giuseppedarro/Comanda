package dev.giuseppedarro.comanda.features.orders.data.repository

import dev.giuseppedarro.comanda.features.orders.data.OrderItems
import dev.giuseppedarro.comanda.features.orders.data.Orders
import dev.giuseppedarro.comanda.features.orders.data.model.SubmitOrderRequest
import dev.giuseppedarro.comanda.features.orders.domain.model.Order
import dev.giuseppedarro.comanda.features.orders.domain.model.OrderItem
import dev.giuseppedarro.comanda.features.orders.domain.model.OrderStatus
import dev.giuseppedarro.comanda.features.orders.domain.repository.OrdersRepository
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.UUID

class OrdersRepositoryImpl : OrdersRepository {

    override suspend fun submitOrder(request: SubmitOrderRequest): Result<Order> {
        val newOrderId = UUID.randomUUID().toString()
        val creationTime = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
        
        return try {
            transaction {
                // 1. Insert the Order
                Orders.insert {
                    it[id] = newOrderId
                    it[tableNumber] = request.tableNumber
                    it[numberOfPeople] = request.numberOfPeople
                    it[status] = OrderStatus.open.name
                    it[createdAt] = creationTime
                    it[subtotal] = null
                    it[serviceCharge] = null
                    it[total] = null
                }

                // 2. Insert the OrderItems
                var itemCounter = 0
                val createdItems = request.items.map { item ->
                    itemCounter++
                    val newItemId = "oi_${newOrderId}_$itemCounter"
                    
                    OrderItems.insert {
                        it[id] = newItemId
                        it[orderId] = newOrderId
                        it[itemId] = item.menuItemId
                        it[quantity] = item.quantity
                        it[notes] = item.notes
                    }
                    
                    OrderItem(
                        orderItemId = newItemId,
                        itemId = item.menuItemId,
                        quantity = item.quantity,
                        notes = item.notes
                    )
                }

                // 3. Return the domain object
                Result.success(
                    Order(
                        id = newOrderId,
                        tableNumber = request.tableNumber,
                        numberOfPeople = request.numberOfPeople,
                        status = OrderStatus.open,
                        items = createdItems,
                        createdAt = creationTime,
                        subtotal = null,
                        serviceCharge = null,
                        total = null
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getOrders(): List<Order> {
        return transaction {
            // Fetch all orders
            val orders = Orders.selectAll().map { row ->
                val orderId = row[Orders.id]
                
                // Fetch items for this order
                val items = OrderItems.select { OrderItems.orderId eq orderId }.map { itemRow ->
                    OrderItem(
                        orderItemId = itemRow[OrderItems.id],
                        itemId = itemRow[OrderItems.itemId],
                        quantity = itemRow[OrderItems.quantity],
                        notes = itemRow[OrderItems.notes]
                    )
                }

                Order(
                    id = orderId,
                    tableNumber = row[Orders.tableNumber],
                    numberOfPeople = row[Orders.numberOfPeople],
                    status = OrderStatus.valueOf(row[Orders.status]),
                    items = items,
                    createdAt = row[Orders.createdAt],
                    subtotal = row[Orders.subtotal],
                    serviceCharge = row[Orders.serviceCharge],
                    total = row[Orders.total]
                )
            }
            orders
        }
    }

    override suspend fun getOrdersForTable(tableNumber: Int): List<Order> {
        return transaction {
            val orders = Orders.select { Orders.tableNumber eq tableNumber }.map { row ->
                val orderId = row[Orders.id]
                
                val items = OrderItems.select { OrderItems.orderId eq orderId }.map { itemRow ->
                    OrderItem(
                        orderItemId = itemRow[OrderItems.id],
                        itemId = itemRow[OrderItems.itemId],
                        quantity = itemRow[OrderItems.quantity],
                        notes = itemRow[OrderItems.notes]
                    )
                }

                Order(
                    id = orderId,
                    tableNumber = row[Orders.tableNumber],
                    numberOfPeople = row[Orders.numberOfPeople],
                    status = OrderStatus.valueOf(row[Orders.status]),
                    items = items,
                    createdAt = row[Orders.createdAt],
                    subtotal = row[Orders.subtotal],
                    serviceCharge = row[Orders.serviceCharge],
                    total = row[Orders.total]
                )
            }
            orders
        }
    }

    override suspend fun getOrderById(orderId: String): Order? {
        return transaction {
            val row = Orders.select { Orders.id eq orderId }.singleOrNull() ?: return@transaction null
            
            val items = OrderItems.select { OrderItems.orderId eq orderId }.map { itemRow ->
                OrderItem(
                    orderItemId = itemRow[OrderItems.id],
                    itemId = itemRow[OrderItems.itemId],
                    quantity = itemRow[OrderItems.quantity],
                    notes = itemRow[OrderItems.notes]
                )
            }

            Order(
                id = row[Orders.id],
                tableNumber = row[Orders.tableNumber],
                numberOfPeople = row[Orders.numberOfPeople],
                status = OrderStatus.valueOf(row[Orders.status]),
                items = items,
                createdAt = row[Orders.createdAt],
                subtotal = row[Orders.subtotal],
                serviceCharge = row[Orders.serviceCharge],
                total = row[Orders.total]
            )
        }
    }
}
