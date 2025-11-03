package dev.giuseppedarro.comanda.core.navigation

import kotlinx.serialization.Serializable

@Serializable
object Login

@Serializable
object Tables

@Serializable
data class MenuOrder(val tableNumber: Int, val numberOfPeople: Int)
