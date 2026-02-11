package dev.giuseppedarro.comanda.features.menu.domain.model

data class MenuItem(
    val id: String,
    val categoryId: String,
    val name: String,
    val description: String,
    val price: Int,
    val isAvailable: Boolean,
    val displayOrder: Int
)
