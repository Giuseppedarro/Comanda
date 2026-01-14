package dev.giuseppedarro.comanda.features.printers.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Printer(
    val id: Int,
    val name: String,
    val address: String,
    val port: Int = 9100
)