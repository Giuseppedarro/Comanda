package dev.giuseppedarro.comanda.features.orders.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MenuItemDto(
    val name: String,
    val price: String
)
