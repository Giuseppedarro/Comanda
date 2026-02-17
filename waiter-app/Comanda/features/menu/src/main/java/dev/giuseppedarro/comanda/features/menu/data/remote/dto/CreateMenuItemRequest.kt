package dev.giuseppedarro.comanda.features.menu.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateMenuItemRequest(
    val name: String,
    val price: Int,
    val description: String,
    val isAvailable: Boolean,
    val displayOrder: Int
)
