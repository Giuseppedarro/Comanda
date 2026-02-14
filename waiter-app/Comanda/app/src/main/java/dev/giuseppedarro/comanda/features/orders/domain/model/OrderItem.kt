package dev.giuseppedarro.comanda.features.orders.domain.model

data class OrderItem(
    val menuItem: MenuItem,
    val quantity: Int = 1,
    val orderItemId: String? = null,
    val notes: String? = null
)
