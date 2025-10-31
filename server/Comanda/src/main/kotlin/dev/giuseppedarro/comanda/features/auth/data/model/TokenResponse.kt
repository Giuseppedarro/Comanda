package dev.giuseppedarro.comanda.features.auth.data.model

import kotlinx.serialization.Serializable

@Serializable
data class TokenResponse(val accessToken: String, val refreshToken: String)
