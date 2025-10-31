package dev.giuseppedarro.comanda.features.orders.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class OrderItem(val menuItem: MenuItem, val quantity: Int = 1)
