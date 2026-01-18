package dev.giuseppedarro.comanda.features.menu.domain.model

data class MenuCategory(
    val name: String,
    val items: List<MenuItem>
)