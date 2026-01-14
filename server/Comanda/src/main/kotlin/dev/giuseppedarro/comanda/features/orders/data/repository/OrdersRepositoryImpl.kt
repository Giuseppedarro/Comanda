package dev.giuseppedarro.comanda.features.orders.data.repository

import dev.giuseppedarro.comanda.features.orders.data.OrderItems
import dev.giuseppedarro.comanda.features.orders.data.Orders
import dev.giuseppedarro.comanda.features.orders.data.model.SubmitOrderRequest
import dev.giuseppedarro.comanda.features.orders.domain.model.Order
import dev.giuseppedarro.comanda.features.orders.domain.model.OrderItem
import dev.giuseppedarro.comanda.features.orders.domain.model.OrderStatus
import dev.giuseppedarro.comanda.features.orders.domain.repository.OrdersRepository
import dev.giuseppedarro.comanda.features.printers.domain.model.Printer
import dev.giuseppedarro.comanda.features.printers.domain.repository.PrintersRepository
import dev.giuseppedarro.comanda.features.printers.domain.usecase.SendTicketToPrinterUseCase
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

class OrdersRepositoryImpl(
    private val sendTicketToPrinterUseCase: SendTicketToPrinterUseCase,
    private val printersRepository: PrintersRepository
) : OrdersRepository {

    companion object {
        private val menuItemNames = mapOf(
            // Appetizers
            "app_bruschetta" to "Bruschetta",
            "app_garlic_bread" to "Garlic Bread",
            "app_mushrooms" to "Stuffed Mushrooms",
            "app_spring_rolls" to "Spring Rolls",
            "app_onion_rings" to "Onion Rings",
            "app_calamari" to "Calamari",
            // Main Courses
            "main_burger" to "Gourmet Burger",
            "main_caesar_salad" to "Caesar Salad",
            // Desserts
            "dess_tiramisu" to "Tiramisu",
            "dess_cheesecake" to "Cheesecake",
            // Drinks
            "drink_cola" to "Cola",
            "drink_cappuccino" to "Cappuccino",
            "drink_iced_tea" to "Iced Tea",
            "drink_orange_juice" to "Orange Juice",
            "drink_latte" to "Latte",
            "drink_water" to "Water",
            "drink_espresso" to "Espresso",
            "drink_lemonade" to "Lemonade",
            "drink_apple_juice" to "Apple Juice",
            "drink_sparkling_water" to "Sparkling Water",
            "drink_green_tea" to "Green Tea",
            "drink_beer" to "Beer"
        )

        private fun getMenuItemName(itemId: String): String {
            return menuItemNames[itemId] ?: itemId
        }
    }

    override suspend fun submitOrder(request: SubmitOrderRequest): Result<Order> {
        val creationTime = DateTimeFormatter.ISO_INSTANT.format(Instant.now())

        return try {
            // 1) Compute deltas inside transaction for DB access
            val deltas = transaction {
                // Find previous order for this table (if any)
                val existingOrder = Orders
                    .select { Orders.tableNumber eq request.tableNumber }
                    .singleOrNull()

                val previousQtyByItem: Map<String, Int> = if (existingOrder != null) {
                    OrderItems
                        .select { OrderItems.tableNumber eq request.tableNumber }
                        .groupBy { it[OrderItems.itemId] }
                        .mapValues { entry -> entry.value.sumOf { it[OrderItems.quantity] } }
                } else emptyMap()

                // Build quantity map from current request
                val currentQtyByItem: Map<String, Int> = request.items
                    .groupBy { it.menuItemId }
                    .mapValues { entry -> entry.value.sumOf { it.quantity } }

                // Compute positive deltas (newly added items compared to previous order)
                currentQtyByItem.mapNotNull { (itemId, qtyNow) ->
                    val qtyPrev = previousQtyByItem[itemId] ?: 0
                    val diff = qtyNow - qtyPrev
                    if (diff > 0) itemId to diff else null
                }
            }

            // 2) Process and print tickets (outside transaction for suspend function support)
            if (deltas.isNotEmpty()) {
                // Build ticket content
                val ticketContent = buildString {
                    append("\n================== NEW ITEMS TICKET ==================\n")
                    append("Table: ${request.tableNumber}    People: ${request.numberOfPeople}    At: $creationTime\n")
                    append("----------------------------------------------------\n")
                    deltas.forEach { (itemId, qty) ->
                        append(String.format("%2dx  %s\n", qty, getMenuItemName(itemId)))
                    }
                    // If there are notes in the current request, list them under their items
                    val notesByItem = request.items.filter { !it.notes.isNullOrBlank() }
                        .groupBy { it.menuItemId }
                        .mapValues { (_, list) -> list.mapNotNull { it.notes }.distinct() }
                    if (notesByItem.isNotEmpty()) {
                        append("----------------------------------------------------\n")
                        append("Notes:\n")
                        notesByItem.forEach { (itemId, notes) ->
                            notes.forEach { note -> append(" - ${getMenuItemName(itemId)}: $note\n") }
                        }
                    }
                    append("====================================================\n")
                }

                // Print to terminal
                println(ticketContent)

                // Send to Kitchen printer (id=1) by default
                // TODO: Later, determine printer based on item categories
                val kitchenPrinterId = 1
                val kitchenPrinter = printersRepository.getPrinterById(kitchenPrinterId)

                if (kitchenPrinter != null) {
                    try {
                        val sendResult = sendTicketToPrinterUseCase(kitchenPrinterId, ticketContent)
                        if (sendResult.isFailure) {
                            println("[ERROR] Printer '${kitchenPrinter.name}' (${kitchenPrinter.address}:${kitchenPrinter.port}) is not connected")
                            println("[FALLBACK] Ticket printed to terminal above")
                        } else {
                            println("[SUCCESS] Ticket sent to '${kitchenPrinter.name}' printer")
                        }
                    } catch (e: Exception) {
                        println("[ERROR] Exception communicating with printer '${kitchenPrinter.name}': ${e.message}")
                        println("[FALLBACK] Ticket printed to terminal above")
                    }
                } else {
                    println("[WARN] Kitchen printer (id=$kitchenPrinterId) not found in database")
                    println("[FALLBACK] Ticket printed to terminal above")
                }
            } else {
                println("[INFO] Table ${request.tableNumber}: no new items compared to previous order.")
            }

            // 3) Save order to database
            val order = transaction {
                // Delete old order items for this table (if any)
                OrderItems.deleteWhere { OrderItems.tableNumber eq request.tableNumber }

                // Insert or replace the Order
                Orders.replace {
                    it[tableNumber] = request.tableNumber
                    it[numberOfPeople] = request.numberOfPeople
                    it[status] = OrderStatus.open.name
                    it[createdAt] = creationTime
                    it[subtotal] = null
                    it[serviceCharge] = null
                    it[total] = null
                }

                // Insert the OrderItems
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

                // Return the created order
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
            }

            Result.success(order)
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