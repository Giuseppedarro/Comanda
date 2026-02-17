package dev.giuseppedarro.comanda.features.login.domain.use_case

import dev.giuseppedarro.comanda.features.login.domain.repository.LoginRepository

class LoginUseCase(private val repository: LoginRepository) {

    suspend operator fun invoke(employeeId: String, password: String): Result<Unit> {
        if (employeeId.isBlank() || password.isBlank()) {
            return Result.failure(Exception("Employee ID and password cannot be empty."))
        }
        return repository.login(employeeId, password)
    }
}
