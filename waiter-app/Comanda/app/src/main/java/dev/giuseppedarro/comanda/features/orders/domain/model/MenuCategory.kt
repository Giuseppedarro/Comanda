package dev.giuseppedarro.comanda.features.orders.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class MenuCategory(
    val name: String,
    val items: List<MenuItem>
)
