package dev.giuseppedarro.comanda.features.orders.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
data class MenuOrder(val tableNumber: Int, val numberOfPeople: Int)
