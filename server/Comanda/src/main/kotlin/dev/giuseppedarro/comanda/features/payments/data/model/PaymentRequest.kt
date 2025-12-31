package dev.giuseppedarro.comanda.features.payments.data.model

import kotlinx.serialization.Serializable

@Serializable
enum class PaymentMethod { CASH, CARD }

@Serializable
data class PaymentRequest(
    val orderId: String,
    val amount: Double,
    val method: PaymentMethod
)
