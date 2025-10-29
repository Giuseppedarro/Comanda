
package dev.giuseppedarro.comanda.auth.domain.usecase

import dev.giuseppedarro.comanda.auth.data.model.RefreshTokenRequest
import dev.giuseppedarro.comanda.auth.domain.repository.AuthRepository

class RefreshTokenUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(request: RefreshTokenRequest) = authRepository.refreshToken(request)
}
