package dev.giuseppedarro.comanda.features.settings.data.repository

import dev.giuseppedarro.comanda.core.data.repository.toDomainException
import dev.giuseppedarro.comanda.core.network.UserApi
import dev.giuseppedarro.comanda.core.network.dto.CreateUserRequest
import dev.giuseppedarro.comanda.core.network.dto.UpdateUserRequest
import dev.giuseppedarro.comanda.features.settings.data.remote.dto.toDomain
import dev.giuseppedarro.comanda.features.settings.domain.model.User
import dev.giuseppedarro.comanda.features.settings.domain.model.UserException
import dev.giuseppedarro.comanda.features.settings.domain.repository.UserRepository
import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class UserRepositoryImpl(private val userApi: UserApi) : UserRepository {

    override suspend fun createUser(request: CreateUserRequest): Result<User> {
        return safeUserApiCall {
            userApi.createUser(request).toDomain()
        }
    }

    override fun getUsers(): Flow<Result<List<User>>> = flow {
        val userProfiles = userApi.getUsers()
        emit(Result.success(userProfiles.map { it.toDomain() }))
    }.catch { e ->
        emit(Result.failure(e.toDomainException()))
    }

    override suspend fun updateUser(id: String, request: UpdateUserRequest): Result<User> {
        return id.toIntOrNull()?.let { userId ->
            safeUserApiCall {
                userApi.updateUser(userId, request).toDomain()
            }
        } ?: Result.failure(UserException.InvalidUserId)
    }

    override suspend fun deleteUser(id: String): Result<Unit> {
        return id.toIntOrNull()?.let { userId ->
            safeUserApiCall {
                userApi.deleteUser(userId)
            }
        } ?: Result.failure(UserException.InvalidUserId)
    }

    private suspend fun <T> safeUserApiCall(call: suspend () -> T): Result<T> {
        return try {
            Result.success(call())
        } catch (e: ClientRequestException) {
            val domainError = when (e.response.status) {
                HttpStatusCode.Conflict -> UserException.DuplicateEmployeeId
                HttpStatusCode.NotFound -> UserException.UserNotFound
                HttpStatusCode.BadRequest,
                HttpStatusCode.UnprocessableEntity -> UserException.InvalidUserData
                else -> e.toDomainException()
            }
            Result.failure(domainError)
        } catch (e: Exception) {
            Result.failure(e.toDomainException())
        }
    }
}
