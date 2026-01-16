package dev.giuseppedarro.comanda.features.menu.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class MenuCategory(
    val id: String,
    val name: String,
    val items: List<MenuItem>
)