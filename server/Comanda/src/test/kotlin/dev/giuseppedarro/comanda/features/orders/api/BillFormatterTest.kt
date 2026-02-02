package dev.giuseppedarro.comanda.features.orders.api

import dev.giuseppedarro.comanda.features.menu.domain.model.MenuItem
import dev.giuseppedarro.comanda.features.orders.domain.model.Order
import dev.giuseppedarro.comanda.features.orders.domain.model.OrderItem
import dev.giuseppedarro.comanda.features.orders.domain.model.OrderStatus
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class BillFormatterTest {

    @Test
    fun `test bill formatting with correct currency and totals`() {
        // Given
        val orderItems = listOf(
            OrderItem("oi1", "burger-id", 2, null),
            OrderItem("oi2", "coke-id", 3, null)
        )
        
        val order = Order(
            tableNumber = 7,
            numberOfPeople = 4,
            status = OrderStatus.open,
            items = orderItems,
            createdAt = "2023-10-27T10:00:00Z"
        )

        val menuItems = mapOf(
            "burger-id" to MenuItem("burger-id", "cat1", "Cheeseburger", 1250, "Delicious", true), // €12.50
            "coke-id" to MenuItem("coke-id", "cat2", "Coca Cola", 300, "Cold", true) // €3.00
        )

        // When
        // Normalize spaces to avoid issues with non-breaking spaces (NBSP) often used in currency formats
        val billOutput = BillFormatter.formatBill(order, menuItems).replace('\u00A0', ' ')

        // Then
        println(billOutput)

        // Assertions to verify correctness
        assertTrue(billOutput.contains("Table: 7"))
        assertTrue(billOutput.contains("People: 4"))
        
        // Check line items
        // Burger: 12.50 * 2 = 25.00
        assertTrue(billOutput.contains("Cheeseburger"))
        assertTrue(billOutput.contains("x2"))
        // Check for "25,00" and "€" separately or in the expected "25,00 €" format
        assertTrue(billOutput.contains("25,00 €") || billOutput.contains("€ 25,00"))

        // Coke: 3.00 * 3 = 9.00
        assertTrue(billOutput.contains("Coca Cola"))
        assertTrue(billOutput.contains("x3"))
        assertTrue(billOutput.contains("9,00 €") || billOutput.contains("€ 9,00"))

        // Check Totals
        // Subtotal: 25.00 + 9.00 = 34.00
        assertTrue(billOutput.contains("Subtotal:"))
        assertTrue(billOutput.contains("34,00 €") || billOutput.contains("€ 34,00"))
    }
}
