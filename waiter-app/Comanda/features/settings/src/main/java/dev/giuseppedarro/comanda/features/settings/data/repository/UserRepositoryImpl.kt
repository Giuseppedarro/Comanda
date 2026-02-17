package dev.giuseppedarro.comanda.features.settings.data.repository

import dev.giuseppedarro.comanda.features.settings.data.remote.UserApi
import dev.giuseppedarro.comanda.features.settings.data.remote.dto.CreateUserRequest
import dev.giuseppedarro.comanda.features.settings.data.remote.dto.UpdateUserRequest
import dev.giuseppedarro.comanda.features.settings.data.remote.dto.toDomain
import dev.giuseppedarro.comanda.features.settings.domain.model.User
import dev.giuseppedarro.comanda.features.settings.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserRepositoryImpl(private val userApi: UserApi) : UserRepository {

    override suspend fun createUser(request: CreateUserRequest): Result<User> {
        return userApi.createUser(request).map { it.toDomain() }
    }

    override fun getUsers(): Flow<Result<List<User>>> = flow {
        val result = userApi.getUsers().map { userResponses ->
            userResponses.map { it.toDomain() }
        }
        emit(result)
    }

    override suspend fun updateUser(id: String, request: UpdateUserRequest): Result<User> {
        return userApi.updateUser(id, request).map { it.toDomain() }
    }

    override suspend fun deleteUser(id: String): Result<Unit> {
        return userApi.deleteUser(id)
    }
}
