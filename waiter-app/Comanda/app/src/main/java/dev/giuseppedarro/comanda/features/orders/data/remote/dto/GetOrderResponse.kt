package dev.giuseppedarro.comanda.features.orders.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class GetOrderResponse(
    val tableNumber: Int,
    val numberOfPeople: Int,
    val items: List<OrderResponseItem>
)
