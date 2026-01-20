package dev.giuseppedarro.comanda.features.menu.presentation

import kotlinx.serialization.Serializable

@Serializable
data class MenuItemDto(val id: String, val categoryId: String, val name: String, val price: Int)

@Serializable
data class MenuCategoryDto(val id: String, val name: String, val items: List<MenuItemDto>)