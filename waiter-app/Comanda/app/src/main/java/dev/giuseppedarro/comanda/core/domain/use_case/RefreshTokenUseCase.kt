package dev.giuseppedarro.comanda.core.domain.use_case

import dev.giuseppedarro.comanda.core.domain.TokenRepository
import dev.giuseppedarro.comanda.features.login.data.dto.LoginResponse
import dev.giuseppedarro.comanda.features.login.data.dto.RefreshTokenRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

/**
 * Use case for refreshing an expired access token using a refresh token.
 * Called by the Ktor Auth plugin when a 401 response is received.
 */
class RefreshTokenUseCase(
    private val client: HttpClient,
    private val tokenRepository: TokenRepository
) {
    /**
     * Attempts to refresh the access token.
     * @param refreshToken The refresh token to use
     * @return Result containing the new access token if successful, or failure
     */
    suspend operator fun invoke(refreshToken: String): Result<String> {
        return try {
            val response = client.post("auth/refresh") {
                contentType(ContentType.Application.Json)
                setBody(RefreshTokenRequest(refreshToken))
            }

            if (response.status.value in 200..299) {
                val loginResponse = response.body<LoginResponse>()
                // Save the new access token
                tokenRepository.saveAccessToken(loginResponse.accessToken)
                Result.success(loginResponse.accessToken)
            } else {
                Result.failure(Exception("Token refresh failed with status ${response.status.value}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}