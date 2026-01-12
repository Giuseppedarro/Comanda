package dev.giuseppedarro.comanda.features.orders.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubmitOrderRequest(
    val tableNumber: Int,
    val numberOfPeople: Int,
    val items: List<OrderItemRequest>
)

@Serializable
data class OrderItemRequest(
    @SerialName("itemId")
    val menuItemId: String,
    val quantity: Int,
    val notes: String? = null
)
