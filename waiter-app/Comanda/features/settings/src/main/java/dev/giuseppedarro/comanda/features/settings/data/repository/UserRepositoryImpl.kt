package dev.giuseppedarro.comanda.features.settings.data.repository

import dev.giuseppedarro.comanda.core.network.UserApi
import dev.giuseppedarro.comanda.core.network.dto.CreateUserRequest
import dev.giuseppedarro.comanda.core.network.dto.UpdateUserRequest
import dev.giuseppedarro.comanda.features.settings.data.remote.dto.toDomain
import dev.giuseppedarro.comanda.features.settings.domain.model.User
import dev.giuseppedarro.comanda.features.settings.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserRepositoryImpl(private val userApi: UserApi) : UserRepository {

    override suspend fun createUser(request: CreateUserRequest): Result<User> {
        return try {
            val profile = userApi.createUser(request)
            Result.success(profile.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getUsers(): Flow<Result<List<User>>> = flow {
        try {
            val userProfiles = userApi.getUsers()
            emit(Result.success(userProfiles.map { it.toDomain() }))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun updateUser(id: String, request: UpdateUserRequest): Result<User> {
        return try {
            val profile = userApi.updateUser(id.toInt(), request)
            Result.success(profile.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteUser(id: String): Result<Unit> {
        return try {
            userApi.deleteUser(id.toInt())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
