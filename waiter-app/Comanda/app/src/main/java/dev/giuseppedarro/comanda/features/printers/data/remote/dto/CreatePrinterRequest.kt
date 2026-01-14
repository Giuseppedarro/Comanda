package dev.giuseppedarro.comanda.features.printers.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreatePrinterRequest(
    val name: String,
    val address: String,
    val port: Int = 9100
)