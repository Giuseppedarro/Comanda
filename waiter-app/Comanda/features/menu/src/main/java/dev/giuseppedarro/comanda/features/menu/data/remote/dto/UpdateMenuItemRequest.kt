package dev.giuseppedarro.comanda.features.menu.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdateMenuItemRequest(
    val name: String,
    val price: Int,
    val categoryId: String,
    val description: String,
    val isAvailable: Boolean,
    val displayOrder: Int
)
