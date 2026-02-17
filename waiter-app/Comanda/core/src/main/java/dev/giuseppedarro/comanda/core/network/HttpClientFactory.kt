package dev.giuseppedarro.comanda.core.network

import dev.giuseppedarro.comanda.core.domain.TokenRepository
import dev.giuseppedarro.comanda.core.network.dto.LoginResponse
import dev.giuseppedarro.comanda.core.network.dto.RefreshTokenRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import java.io.IOException
import kotlinx.serialization.json.Json

data class HttpClientConfig(
    val refreshTokenPath: String = "auth/refresh",
    val logLevel: LogLevel = LogLevel.ALL
)

fun createHttpClient(
    baseUrlProvider: BaseUrlProvider,
    tokenRepository: TokenRepository,
    logger: KtorLogger,
    config: HttpClientConfig = HttpClientConfig(),
    basicClient: HttpClient = createBasicHttpClient(baseUrlProvider, logger, config)
): HttpClient {
    return HttpClient(CIO) {
        expectSuccess = false

        install(Logging) {
            level = config.logLevel
            this.logger = object : Logger {
                override fun log(message: String) {
                    logger.log("KTOR_CLIENT", message)
                }
            }
        }

        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }

        install(Auth) {
            bearer {
                loadTokens {
                    val accessToken = tokenRepository.getAccessToken()
                    val refreshToken = tokenRepository.getRefreshToken()
                    if (accessToken != null && refreshToken != null) {
                        BearerTokens(accessToken, refreshToken)
                    } else {
                        null
                    }
                }

                refreshTokens {                    val tokenToRefresh = oldTokens?.refreshToken
                    if (tokenToRefresh.isNullOrBlank()) {
                        // Don't attempt to refresh with an invalid token.
                        // Clear any lingering tokens and fail the auth.
                        tokenRepository.clear()
                        null
                    } else {
                        handleTokenRefresh(
                            basicClient = basicClient,
                            refreshToken = tokenToRefresh,
                            refreshPath = config.refreshTokenPath,
                            tokenRepository = tokenRepository,
                            logger = logger
                        )
                    }
                }
            }
        }

        defaultRequest {
            url(baseUrlProvider.getBaseUrl())
        }
    }
}

suspend fun handleTokenRefresh(
    basicClient: HttpClient,
    refreshToken: String,
    refreshPath: String,
    tokenRepository: TokenRepository,
    logger: KtorLogger
): BearerTokens? {
    return try {
        val response = basicClient.post(refreshPath) {
            contentType(ContentType.Application.Json)
            setBody(RefreshTokenRequest(refreshToken))
        }

        if (response.status.isSuccess()) {
            val loginResponse = response.body<LoginResponse>()
            tokenRepository.saveAccessToken(loginResponse.accessToken)
            BearerTokens(loginResponse.accessToken, refreshToken)
        } else {
            logger.error("AUTH", "Token refresh failed with status ${response.status.value}")
            tokenRepository.clear()
            null
        }
    } catch (e: Exception) {
        logger.error("AUTH", "Token refresh failed", e)
        if (e !is IOException) {
            tokenRepository.clear()
        }
        null
    }
}

fun createBasicHttpClient(
    baseUrlProvider: BaseUrlProvider,
    logger: KtorLogger,
    config: HttpClientConfig = HttpClientConfig()
): HttpClient {
    return HttpClient(CIO) {
        expectSuccess = false

        install(Logging) {
            level = config.logLevel
            this.logger = object : Logger {
                override fun log(message: String) {
                    logger.log("KTOR_CLIENT_BASIC", message)
                }
            }
        }

        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }

        defaultRequest {
            url(baseUrlProvider.getBaseUrl())
        }
    }
}