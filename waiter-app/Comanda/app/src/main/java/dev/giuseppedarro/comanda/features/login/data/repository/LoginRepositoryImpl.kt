package dev.giuseppedarro.comanda.features.login.data.repository

import dev.giuseppedarro.comanda.core.domain.TokenRepository
import dev.giuseppedarro.comanda.features.login.data.dto.LoginRequest
import dev.giuseppedarro.comanda.features.login.data.dto.LoginResponse
import dev.giuseppedarro.comanda.features.login.domain.repository.LoginRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

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

            if (response.status.value in 200..299) {
                val loginResponse = response.body<LoginResponse>()
                tokenStorage.saveTokens(
                    access = loginResponse.accessToken,
                    refresh = loginResponse.refreshToken
                )
                Result.success(Unit)
            } else {
                val errorBody = response.body<String>()
                Result.failure(Exception(errorBody))
            }
        } catch (e: Exception) {
            Result.failure(Exception("A network error occurred. Please try again."))
        }
    }
}
