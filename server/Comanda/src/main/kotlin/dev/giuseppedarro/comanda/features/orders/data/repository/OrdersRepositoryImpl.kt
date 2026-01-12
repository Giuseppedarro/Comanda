package dev.giuseppedarro.comanda.features.orders.data.repository

import dev.giuseppedarro.comanda.features.orders.data.OrderItems
import dev.giuseppedarro.comanda.features.orders.data.Orders
import dev.giuseppedarro.comanda.features.orders.data.model.SubmitOrderRequest
import dev.giuseppedarro.comanda.features.orders.domain.model.Order
import dev.giuseppedarro.comanda.features.orders.domain.model.OrderItem
import dev.giuseppedarro.comanda.features.orders.domain.model.OrderStatus
import dev.giuseppedarro.comanda.features.orders.domain.repository.OrdersRepository
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.replace
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.time.format.DateTimeFormatter

class OrdersRepositoryImpl : OrdersRepository {

    override suspend fun submitOrder(request: SubmitOrderRequest): Result<Order> {
        val creationTime = DateTimeFormatter.ISO_INSTANT.format(Instant.now())

        return try {
            transaction {
                // 1) Find previous order for this table (if any)
                val existingOrder = Orders
                    .select { Orders.tableNumber eq request.tableNumber }
                    .singleOrNull()

                val previousQtyByItem: Map<String, Int> = if (existingOrder != null) {
                    OrderItems
                        .select { OrderItems.tableNumber eq request.tableNumber }
                        .groupBy { it[OrderItems.itemId] }
                        .mapValues { entry -> entry.value.sumOf { it[OrderItems.quantity] } }
                } else emptyMap()

                // 2) Build quantity map from current request
                val currentQtyByItem: Map<String, Int> = request.items
                    .groupBy { it.menuItemId }
                    .mapValues { entry -> entry.value.sumOf { it.quantity } }

                // 3) Compute positive deltas (newly added items compared to previous order)
                val deltas: List<Pair<String, Int>> = currentQtyByItem.mapNotNull { (itemId, qtyNow) ->
                    val qtyPrev = previousQtyByItem[itemId] ?: 0
                    val diff = qtyNow - qtyPrev
                    if (diff > 0) itemId to diff else null
                }

                // 4) Print to terminal (simulating kitchen/bar ticket)
                if (deltas.isNotEmpty()) {
                    println("\n================== NEW ITEMS TICKET ==================")
                    println("Table: ${request.tableNumber}    People: ${request.numberOfPeople}    At: $creationTime")
                    println("----------------------------------------------------")
                    deltas.forEach { (itemId, qty) ->
                        println(String.format("%2dx  %s", qty, itemId))
                    }
                    // If there are notes in the current request, list them under their items
                    val notesByItem = request.items.filter { !it.notes.isNullOrBlank() }
                        .groupBy { it.menuItemId }
                        .mapValues { (_, list) -> list.mapNotNull { it.notes }.distinct() }
                    if (notesByItem.isNotEmpty()) {
                        println("----------------------------------------------------")
                        println("Notes:")
                        notesByItem.forEach { (itemId, notes) ->
                            notes.forEach { note -> println(" - $itemId: $note") }
                        }
                    }
                    println("====================================================\n")
                } else {
                    println("[INFO] Table ${request.tableNumber}: no new items compared to previous order.")
                }

                // 5) Delete old order items for this table (if any)
                OrderItems.deleteWhere { OrderItems.tableNumber eq request.tableNumber }

                // 6) Insert or replace the Order
                Orders.replace {
                    it[tableNumber] = request.tableNumber
                    it[numberOfPeople] = request.numberOfPeople
                    it[status] = OrderStatus.open.name
                    it[createdAt] = creationTime
                    it[subtotal] = null
                    it[serviceCharge] = null
                    it[total] = null
                }

                // 7) Insert the OrderItems
                var itemCounter = 0
                val createdItems = request.items.map { item ->
                    itemCounter++
                    val newItemId = "oi_${request.tableNumber}_${System.nanoTime()}_$itemCounter"

                    OrderItems.insert {
                        it[id] = newItemId
                        it[tableNumber] = request.tableNumber
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

                // 8. Return the domain object
                Result.success(
                    Order(
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
                val tableNum = row[Orders.tableNumber]

                // Fetch items for this order
                val items = OrderItems.select { OrderItems.tableNumber eq tableNum }.map { itemRow ->
                    OrderItem(
                        orderItemId = itemRow[OrderItems.id],
                        itemId = itemRow[OrderItems.itemId],
                        quantity = itemRow[OrderItems.quantity],
                        notes = itemRow[OrderItems.notes]
                    )
                }

                Order(
                    tableNumber = tableNum,
                    numberOfPeople = row[Orders.numberOfPeople],
                    status = OrderStatus.valueOf(row[Orders.status]),
                    items = items,
                    createdAt = row[Orders.createdAt],
                    subtotal = row[Orders.subtotal],
                    serviceCharge = row[Orders.serviceCharge],
                    total = row[Orders.total]
                )
            }
            // Ensure deterministic ordering for clients: latest first
            orders.sortedByDescending { it.createdAt }
        }
    }

    override suspend fun getOrdersForTable(tableNumber: Int): List<Order> {
        return transaction {
            val orders = Orders.select { Orders.tableNumber eq tableNumber }.map { row ->
                val tableNum = row[Orders.tableNumber]

                val items = OrderItems.select { OrderItems.tableNumber eq tableNum }.map { itemRow ->
                    OrderItem(
                        orderItemId = itemRow[OrderItems.id],
                        itemId = itemRow[OrderItems.itemId],
                        quantity = itemRow[OrderItems.quantity],
                        notes = itemRow[OrderItems.notes]
                    )
                }

                Order(
                    tableNumber = tableNum,
                    numberOfPeople = row[Orders.numberOfPeople],
                    status = OrderStatus.valueOf(row[Orders.status]),
                    items = items,
                    createdAt = row[Orders.createdAt],
                    subtotal = row[Orders.subtotal],
                    serviceCharge = row[Orders.serviceCharge],
                    total = row[Orders.total]
                )
            }
            // Latest order first to simplify client selection
            orders.sortedByDescending { it.createdAt }
        }
    }

    override suspend fun getOrderById(orderId: String): Order? {
        // Since we now use tableNumber as the primary key, orderId should be interpreted as tableNumber
        val tableNumber = orderId.toIntOrNull() ?: return null

        return transaction {
            val row = Orders.select { Orders.tableNumber eq tableNumber }.singleOrNull() ?: return@transaction null

            val items = OrderItems.select { OrderItems.tableNumber eq tableNumber }.map { itemRow ->
                OrderItem(
                    orderItemId = itemRow[OrderItems.id],
                    itemId = itemRow[OrderItems.itemId],
                    quantity = itemRow[OrderItems.quantity],
                    notes = itemRow[OrderItems.notes]
                )
            }

            Order(
                tableNumber = tableNumber,
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