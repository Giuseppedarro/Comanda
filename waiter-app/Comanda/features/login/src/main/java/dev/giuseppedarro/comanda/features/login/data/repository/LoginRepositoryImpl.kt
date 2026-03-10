package dev.giuseppedarro.comanda.features.login.data.repository

import dev.giuseppedarro.comanda.core.data.repository.toDomainException
import dev.giuseppedarro.comanda.core.domain.model.DomainException
import dev.giuseppedarro.comanda.core.domain.repository.TokenRepository
import dev.giuseppedarro.comanda.core.network.dto.LoginResponse
import dev.giuseppedarro.comanda.features.login.data.dto.LoginRequest
import dev.giuseppedarro.comanda.features.login.domain.model.LoginException
import dev.giuseppedarro.comanda.features.login.domain.repository.LoginRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.isSuccess

class LoginRepositoryImpl(
    private val client: HttpClient,
    private val tokenStorage: TokenRepository,
) : LoginRepository {

    override suspend fun login(employeeId: String, password: String): Result<Unit> {
        return try {
            val response = client.post("auth/login") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(employeeId, password))
            }

            if (response.status.isSuccess()) {
                val loginResponse = response.body<LoginResponse>()
                tokenStorage.saveTokens(
                    access = loginResponse.accessToken,
                    refresh = loginResponse.refreshToken
                )
                Result.success(Unit)
            } else {
                val domainError = when (response.status) {
                    HttpStatusCode.Unauthorized -> LoginException.InvalidCredentials
                    else -> mapHttpStatusCode(response.status)
                }
                Result.failure(domainError)
            }
        } catch (e: Exception) {
            Result.failure(e.toDomainException())
        }
    }

    private fun mapHttpStatusCode(status: HttpStatusCode): DomainException {
        return when (status) {
            HttpStatusCode.Unauthorized -> LoginException.InvalidCredentials
            HttpStatusCode.NotFound -> DomainException.NotFoundException
            HttpStatusCode.InternalServerError -> DomainException.ServerException
            else -> DomainException.UnknownException("Status: ${status.value}")
        }
    }
}
