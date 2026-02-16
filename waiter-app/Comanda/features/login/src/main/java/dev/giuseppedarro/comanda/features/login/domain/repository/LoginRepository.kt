package dev.giuseppedarro.comanda.features.login.domain.repository

interface LoginRepository {
    suspend fun login(employeeId: String, password: String): Result<Unit>
}
