package dev.giuseppedarro.comanda.features.menu.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MenuItemDto(
    val id: String,
    val name: String,
    val price: Int,
    val categoryId: String? = null,
    val description: String? = null,
    val isAvailable: Boolean? = null,
    val displayOrder: Int? = null
)
