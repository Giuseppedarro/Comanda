package dev.giuseppedarro.comanda.features.settings.domain.repository

import dev.giuseppedarro.comanda.features.settings.data.remote.dto.CreateUserRequest
import dev.giuseppedarro.comanda.features.settings.data.remote.dto.UpdateUserRequest
import dev.giuseppedarro.comanda.features.settings.data.remote.dto.UserResponse
import dev.giuseppedarro.comanda.features.settings.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun createUser(request: CreateUserRequest): Result<UserResponse>
    fun getUsers(): Flow<Result<List<User>>>
    suspend fun updateUser(id: String, request: UpdateUserRequest): Result<User>
    suspend fun deleteUser(id: String): Result<Unit>
}
