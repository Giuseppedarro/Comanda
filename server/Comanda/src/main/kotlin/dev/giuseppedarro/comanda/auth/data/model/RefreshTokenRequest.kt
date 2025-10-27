
package dev.giuseppedarro.comanda.auth.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequest(val refreshToken: String)
