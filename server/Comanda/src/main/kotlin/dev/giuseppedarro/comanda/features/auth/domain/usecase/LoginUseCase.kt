package dev.giuseppedarro.comanda.features.auth.domain.usecase

import dev.giuseppedarro.comanda.features.auth.data.model.LoginRequest
import dev.giuseppedarro.comanda.features.auth.domain.repository.AuthRepository

class LoginUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(request: LoginRequest) = authRepository.login(request)
}
