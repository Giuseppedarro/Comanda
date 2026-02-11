package dev.giuseppedarro.comanda.features.settings.domain.repository

import dev.giuseppedarro.comanda.features.settings.data.remote.dto.CreateUserRequest
import dev.giuseppedarro.comanda.features.settings.data.remote.dto.UserResponse

interface UserRepository {
    suspend fun createUser(request: CreateUserRequest): Result<UserResponse>
}
