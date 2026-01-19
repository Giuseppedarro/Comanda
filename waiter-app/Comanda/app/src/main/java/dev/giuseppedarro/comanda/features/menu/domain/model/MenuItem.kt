package dev.giuseppedarro.comanda.features.menu.domain.model

data class MenuItem(
    val id: String,
    val categoryId: String, // Required when moving an item
    val name: String,
    val description: String,
    val price: Double,
    val isAvailable: Boolean,
    val displayOrder: Int
)
