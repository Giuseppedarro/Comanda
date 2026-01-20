package dev.giuseppedarro.comanda.features.menu.domain.model

data class MenuCategory(
    val id: String,
    val name: String,
    val displayOrder: Int,
    val items: List<MenuItem>
)
