package dev.giuseppedarro.comanda.features.menu.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MenuCategoryDto(
    val name: String,
    val items: List<MenuItemDto>
)