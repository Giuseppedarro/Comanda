package dev.giuseppedarro.comanda.core.network

import android.util.Log
import dev.giuseppedarro.comanda.core.domain.TokenRepository
import dev.giuseppedarro.comanda.core.domain.use_case.RefreshTokenUseCase
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Creates and configures the shared HttpClient with JWT authentication.
 *
 * Note: RefreshTokenUseCase must use a separate client without Auth configuration
 * to avoid circular dependencies.
 */
fun createHttpClient(
    baseUrlProvider: BaseUrlProvider,
    tokenRepository: TokenRepository,
    refreshTokenUseCase: RefreshTokenUseCase
): HttpClient {
    return HttpClient(CIO) {
        expectSuccess = false

        // Logging
        install(Logging) {
            level = LogLevel.ALL
            logger = object : Logger {
                override fun log(message: String) {
                    Log.d("KTOR_CLIENT", message)
                }
            }
        }

        // JSON Serialization
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }

        // JWT Authentication with automatic token refresh
        install(Auth) {
            bearer {
                // Load tokens from storage for every request
                loadTokens {
                    val accessToken = tokenRepository.getAccessToken()
                    val refreshToken = tokenRepository.getRefreshToken()
                    if (accessToken != null && refreshToken != null) {
                        BearerTokens(accessToken, refreshToken)
                    } else {
                        null
                    }
                }

                // Automatically refresh tokens when a 401 is received
                refreshTokens {
                    oldTokens?.refreshToken?.let { refreshToken ->
                        try {
                            val result = refreshTokenUseCase(refreshToken)
                            if (result.isSuccess) {
                                val newAccessToken = result.getOrNull()!!
                                // Return new tokens - refresh token stays the same
                                BearerTokens(newAccessToken, refreshToken)
                            } else {
                                // Refresh failed - clear tokens
                                tokenRepository.clear()
                                null
                            }
                        } catch (e: Exception) {
                            Log.e("AUTH", "Token refresh failed", e)
                            tokenRepository.clear()
                            null
                        }
                    }
                }
            }
        }

        // Default Request
        defaultRequest {
            // Read current base URL dynamically for each request
            url(baseUrlProvider.getBaseUrl())
        }
    }
}

/**
 * Creates a basicHttpClient without authentication for use in scenarios
 * where Auth is not needed (e.g., login, token refresh) to avoid circular dependencies.
 */
fun createBasicHttpClient(
    baseUrlProvider: BaseUrlProvider
): HttpClient {
    return HttpClient(CIO) {
        expectSuccess = false

        // Logging
        install(Logging) {
            level = LogLevel.ALL
            logger = object : Logger {
                override fun log(message: String) {
                    Log.d("KTOR_CLIENT_BASIC", message)
                }
            }
        }

        // JSON Serialization
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }

        defaultRequest {
            url(baseUrlProvider.getBaseUrl())
        }
    }
}