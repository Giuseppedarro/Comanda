package dev.giuseppedarro.comanda.features.payments.data.model

import kotlinx.serialization.Serializable

@Serializable
data class BillResponse(
    val orderId: String,
    val tableNumber: Int,
    val items: List<BillItem>,
    val subtotal: Double,
    val serviceCharge: Double,
    val total: Double
)

@Serializable
data class BillItem(
    val name: String,
    val quantity: Int,
    val price: Double,
    val total: Double
)
