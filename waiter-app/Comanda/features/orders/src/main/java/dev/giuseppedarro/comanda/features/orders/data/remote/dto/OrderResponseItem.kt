package dev.giuseppedarro.comanda.features.orders.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class OrderResponseItem(
    val orderItemId: String,    // Unique ID for this item row
    val itemId: String,         // Menu item ID
    val quantity: Int,
    val notes: String? = null   // Any notes for the item
)