package dev.giuseppedarro.comanda.features.orders.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class OrderStatus { open, served, closed }

@Serializable
data class OrderItem(
    val orderItemId: String,
    val itemId: String,
    val quantity: Int,
    val notes: String? = null
)

@Serializable
data class Order(
    val tableNumber: Int,
    val numberOfPeople: Int,
    val status: OrderStatus = OrderStatus.open,
    val items: List<OrderItem>,
    val createdAt: String,
    val subtotal: Int? = null,
    val serviceCharge: Int? = null,
    val total: Int? = null
)
