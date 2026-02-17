package dev.giuseppedarro.comanda.features.tables.data.remote.dto

import dev.giuseppedarro.comanda.features.tables.domain.model.Table
import kotlinx.serialization.Serializable

@Serializable
data class TableDto(
    val number: Int,
    val isOccupied: Boolean
)

fun TableDto.toDomain(): Table {
    return Table(
        number = number,
        isOccupied = isOccupied
    )
}