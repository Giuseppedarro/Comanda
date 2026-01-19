package dev.giuseppedarro.comanda.features.menu.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MenuCategoryDto(
    val id: String? = null,
    val name: String,
    val displayOrder: Int,
    val items: List<MenuItemDto>
)
