package dev.giuseppedarro.comanda.features.menu.presentation

import kotlinx.serialization.Serializable

@Serializable
data class MenuItemUpdateDto(
    val id: String,
    val categoryId: String,
    val name: String,
    val price: Int,
    val description: String? = null,
    val isAvailable: Boolean = true,
    val displayOrder: Int = 0
)

@Serializable
data class MenuCategoryUpdateDto(
    val id: String,
    val name: String,
    val displayOrder: Int = 0,
    val items: List<MenuItemUpdateDto>
)

@Serializable
data class MenuUpdateRequest(
    val categories: List<MenuCategoryUpdateDto>
)