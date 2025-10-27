
package dev.giuseppedarro.comanda.auth.domain.repository

import dev.giuseppedarro.comanda.auth.data.model.LoginRequest
import dev.giuseppedarro.comanda.auth.data.model.LoginResponse
import dev.giuseppedarro.comanda.auth.data.model.RefreshTokenRequest
import dev.giuseppedarro.comanda.auth.data.model.TokenResponse

interface AuthRepository {
    suspend fun login(request: LoginRequest): Result<LoginResponse>
    suspend fun refreshToken(request: RefreshTokenRequest): Result<TokenResponse>
}
