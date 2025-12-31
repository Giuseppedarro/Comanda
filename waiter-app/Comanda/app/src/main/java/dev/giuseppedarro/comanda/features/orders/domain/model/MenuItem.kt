package dev.giuseppedarro.comanda.features.orders.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class MenuItem(
    val id: String,
    val name: String, 
    val price: String
)
