package dev.giuseppedarro.comanda.features.orders.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class OrderItemRequest(
    val menuItemId: String, // Assuming the 'name' of the MenuItem will be used as the ID
    val quantity: Int
)
