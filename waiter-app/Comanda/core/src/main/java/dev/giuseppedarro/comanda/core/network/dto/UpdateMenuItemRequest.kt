package dev.giuseppedarro.comanda.core.network.dto

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
