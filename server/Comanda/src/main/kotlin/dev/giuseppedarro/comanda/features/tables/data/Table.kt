package dev.giuseppedarro.comanda.features.tables.data

import kotlinx.serialization.Serializable

@Serializable
data class Table(
    val number: Int,
    val isOccupied: Boolean
)
