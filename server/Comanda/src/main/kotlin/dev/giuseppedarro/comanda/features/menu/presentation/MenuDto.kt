package dev.giuseppedarro.comanda.features.menu.presentation

import kotlinx.serialization.Serializable

@Serializable
data class MenuItemDto(val id: String, val name: String, val price: String)

@Serializable
data class MenuCategoryDto(val name: String, val items: List<MenuItemDto>)