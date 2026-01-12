package dev.giuseppedarro.comanda.features.orders.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MenuItemDto(
    val id: String? = null,  // Optional: backend menu endpoint may not provide it yet
    val name: String,
    val price: String
)
