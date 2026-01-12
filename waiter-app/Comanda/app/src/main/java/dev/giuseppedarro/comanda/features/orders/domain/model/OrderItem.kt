package dev.giuseppedarro.comanda.features.orders.domain.model

data class OrderItem(
    val menuItem: MenuItem,
    val quantity: Int = 1,
    val orderItemId: String? = null,   // Unique ID for this item row (from backend)
    val notes: String? = null          // Any notes for the item
)
