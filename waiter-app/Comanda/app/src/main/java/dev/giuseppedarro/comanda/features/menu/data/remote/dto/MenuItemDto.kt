package dev.giuseppedarro.comanda.features.menu.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MenuItemDto(
    val id: String? = null,
    val name: String,
    val price: String
)