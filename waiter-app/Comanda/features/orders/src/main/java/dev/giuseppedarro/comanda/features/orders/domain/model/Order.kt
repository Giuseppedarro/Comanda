package dev.giuseppedarro.comanda.features.orders.domain.model

enum class OrderStatus(val displayName: String) {
    OPEN("Open"),
    SERVED("Served"),
    CLOSED("Closed");

    companion object {
        fun fromString(value: String): OrderStatus {
            return entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: OPEN
        }
    }
}

data class Order(
    val tableNumber: Int,
    val numberOfPeople: Int,
    val status: OrderStatus,
    val items: List<OrderItem>,
    val createdAt: String,
    val subtotal: Int? = null,
    val serviceCharge: Int? = null,
    val total: Int? = null
)