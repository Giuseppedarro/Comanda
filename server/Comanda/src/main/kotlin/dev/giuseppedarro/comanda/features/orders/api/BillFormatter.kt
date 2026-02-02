package dev.giuseppedarro.comanda.features.orders.api

import dev.giuseppedarro.comanda.features.menu.domain.model.MenuItem
import dev.giuseppedarro.comanda.features.orders.domain.model.Order
import java.text.NumberFormat
import java.util.Locale

object BillFormatter {

    fun formatBill(order: Order, itemsDetails: Map<String, MenuItem>): String {
        val sb = StringBuilder()
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale.ITALY)

        sb.appendLine("\n--- BILL ---")
        sb.appendLine("Table: ${order.tableNumber}")
        sb.appendLine("People: ${order.numberOfPeople}")
        sb.appendLine("--------------------------------")

        var calculatedSubtotal = 0

        order.items.forEach { item ->
            val menuItem = itemsDetails[item.itemId]
            val name = menuItem?.name ?: "Unknown Item (${item.itemId})"
            val price = menuItem?.price ?: 0
            val lineTotal = price * item.quantity
            calculatedSubtotal += lineTotal

            val formattedPrice = formatPrice(lineTotal)
            // Format: "Burger x2      €10,00"
            // Name takes 20 chars, Quantity takes 4 chars
            val line = "${name.take(20).padEnd(20)} x${item.quantity.toString().padEnd(3)} $formattedPrice"
            sb.appendLine(line)
        }

        // Calculate totals
        // Assuming service charge is 0 for now as it's not in the DB logic yet, 
        // but we can easily add a percentage here if needed.
        val serviceCharge = 0 
        val total = calculatedSubtotal + serviceCharge

        sb.appendLine("--------------------------------")
        sb.appendLine("Subtotal:       ${formatPrice(calculatedSubtotal)}")
        sb.appendLine("Service Charge: ${formatPrice(serviceCharge)}")
        sb.appendLine("Total:          ${formatPrice(total)}")
        sb.appendLine("--------------------------------\n")

        return sb.toString()
    }

    fun formatPrice(priceInCents: Int): String {
        val priceInEur = priceInCents / 100.0
        return NumberFormat.getCurrencyInstance(Locale.ITALY).format(priceInEur)
    }
}
