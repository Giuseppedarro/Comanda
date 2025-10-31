package dev.giuseppedarro.comanda.features.orders.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class MenuItem(val name: String, val price: String)
