package dev.giuseppedarro.comanda.features.printers.data.remote.dto

import dev.giuseppedarro.comanda.features.printers.domain.model.Printer
import kotlinx.serialization.Serializable

@Serializable
data class PrinterDto(
    val id: Int,
    val name: String,
    val address: String,
    val port: Int = 9100
)

fun PrinterDto.toDomain(): Printer {
    return Printer(
        id = id,
        name = name,
        address = address,
        port = port
    )
}