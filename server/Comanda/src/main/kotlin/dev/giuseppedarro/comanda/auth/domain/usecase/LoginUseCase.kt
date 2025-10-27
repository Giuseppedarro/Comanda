
package dev.giuseppedarro.comanda.auth.domain.usecase

import dev.giuseppedarro.comanda.auth.data.model.LoginRequest
import dev.giuseppedarro.comanda.auth.domain.repository.AuthRepository

class LoginUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(request: LoginRequest) = authRepository.login(request)
}
