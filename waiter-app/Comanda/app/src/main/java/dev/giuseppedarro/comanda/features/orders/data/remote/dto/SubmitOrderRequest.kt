package dev.giuseppedarro.comanda.features.orders.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class SubmitOrderRequest(
    val tableNumber: Int,
    val numberOfPeople: Int,
    val items: List<OrderItemRequest>
)
