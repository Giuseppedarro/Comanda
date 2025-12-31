package dev.giuseppedarro.comanda.features.menu.data.model

import kotlinx.serialization.Serializable

@Serializable
data class MenuCategoryResponse(
    val name: String,
    val items: List<MenuItemResponse>
)

@Serializable
data class MenuItemResponse(
    val id: String, // Added ID so the frontend can send it back in orders
    val name: String,
    val price: String
)
