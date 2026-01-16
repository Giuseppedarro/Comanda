package dev.giuseppedarro.comanda.features.menu.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class MenuItem(
    val id: String,
    val categoryId: String,
    val name: String,
    val price: Double,
    val description: String? = null,
    val isAvailable: Boolean = true
)