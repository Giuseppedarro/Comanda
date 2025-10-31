package dev.giuseppedarro.comanda.features.auth.domain.usecase

import dev.giuseppedarro.comanda.features.auth.data.model.RefreshTokenRequest
import dev.giuseppedarro.comanda.features.auth.domain.repository.AuthRepository

class RefreshTokenUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(request: RefreshTokenRequest) = authRepository.refreshToken(request)
}
