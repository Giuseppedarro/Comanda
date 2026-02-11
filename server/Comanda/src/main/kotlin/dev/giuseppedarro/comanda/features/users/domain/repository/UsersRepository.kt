package dev.giuseppedarro.comanda.features.users.domain.repository

import dev.giuseppedarro.comanda.features.users.domain.model.User

interface UsersRepository {
    suspend fun createUser(employeeId: String, name: String, passwordHash: String, role: String): Result<User>
    suspend fun getUserByEmployeeId(employeeId: String): User?
}
