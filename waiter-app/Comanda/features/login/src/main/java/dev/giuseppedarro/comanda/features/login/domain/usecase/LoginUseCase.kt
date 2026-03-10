package dev.giuseppedarro.comanda.features.login.domain.usecase

import dev.giuseppedarro.comanda.features.login.domain.model.LoginException
import dev.giuseppedarro.comanda.features.login.domain.repository.LoginRepository

class LoginUseCase(private val repository: LoginRepository) {

    suspend operator fun invoke(employeeId: String, password: String): Result<Unit> {
        if (employeeId.isBlank() || password.isBlank()) {
            return Result.failure(LoginException.EmptyCredentials)
        }
        return repository.login(employeeId, password)
    }
}
