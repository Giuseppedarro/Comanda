package dev.giuseppedarro.comanda.features.orders.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderResponseItem(
    @SerialName("itemId") val menuItemId: String,
    val quantity: Int
)
