package dev.giuseppedarro.comanda.features.auth.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequest(val refreshToken: String)
