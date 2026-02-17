package dev.giuseppedarro.comanda.features.orders.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class GetOrderResponse(
    val tableNumber: Int,
    val numberOfPeople: Int,
    val status: String, // "open" | "served" | "closed"
    val items: List<OrderResponseItem>,
    val createdAt: String, // ISO instant timestamp
    val subtotal: Int? = null,
    val serviceCharge: Int? = null,
    val total: Int? = null
)