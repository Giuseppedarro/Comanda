package dev.giuseppedarro.comanda.features.orders.data.remote.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class OrderItemRequest(
    @SerialName("itemId") val menuItemId: String,
    val quantity: Int,
    val notes: String? = null
)
