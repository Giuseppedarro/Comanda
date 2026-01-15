package dev.giuseppedarro.comanda.features.login.data.dto

import kotlinx.serialization.Serializable

/**
 * Request body for the refresh token endpoint.
 */
@Serializable
data class RefreshTokenRequest(
    val refreshToken: String
)