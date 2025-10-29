
package dev.giuseppedarro.comanda.auth.data.repository

import dev.giuseppedarro.comanda.auth.data.datasource.AuthDataSource
import dev.giuseppedarro.comanda.auth.data.model.LoginRequest
import dev.giuseppedarro.comanda.auth.data.model.LoginResponse
import dev.giuseppedarro.comanda.auth.data.model.RefreshTokenRequest
import dev.giuseppedarro.comanda.auth.data.model.TokenResponse
import dev.giuseppedarro.comanda.auth.domain.repository.AuthRepository

class AuthRepositoryImpl(private val authDataSource: AuthDataSource) : AuthRepository {
    override suspend fun login(request: LoginRequest): Result<LoginResponse> {
        return authDataSource.login(request)
    }

    override suspend fun refreshToken(request: RefreshTokenRequest): Result<TokenResponse> {
        return authDataSource.refreshToken(request)
    }
}
