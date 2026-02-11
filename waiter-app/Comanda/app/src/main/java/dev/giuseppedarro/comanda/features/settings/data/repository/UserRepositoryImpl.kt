package dev.giuseppedarro.comanda.features.settings.data.repository

import dev.giuseppedarro.comanda.features.settings.data.remote.UserApi
import dev.giuseppedarro.comanda.features.settings.data.remote.dto.CreateUserRequest
import dev.giuseppedarro.comanda.features.settings.data.remote.dto.UserResponse
import dev.giuseppedarro.comanda.features.settings.domain.repository.UserRepository

class UserRepositoryImpl(private val userApi: UserApi) : UserRepository {

    override suspend fun createUser(request: CreateUserRequest): Result<UserResponse> {
        return userApi.createUser(request)
    }
}
