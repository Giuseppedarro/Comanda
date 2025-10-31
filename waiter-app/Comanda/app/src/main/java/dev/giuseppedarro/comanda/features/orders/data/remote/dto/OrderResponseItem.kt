package dev.giuseppedarro.comanda.features.orders.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class OrderResponseItem(
    val menuItemId: String,
    val quantity: Int
)
