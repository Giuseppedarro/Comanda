package dev.giuseppedarro.comanda.features.menu.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MenuItemDto(
    val id: String? = null,
    val categoryId: String? = null, // Required when moving an item
    val name: String,
    val description: String,
    val price: Double,
    val isAvailable: Boolean,
    val displayOrder: Int
)
