package dev.giuseppedarro.comanda.features.printers.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UpdatePrinterRequest(
    val name: String,
    val address: String,
    val port: Int = 9100
)