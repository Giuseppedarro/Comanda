package dev.giuseppedarro.comanda.core.network

import com.google.common.truth.Truth.assertThat
import dev.giuseppedarro.comanda.core.domain.TokenRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.justRun
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Test

class HttpClientFactoryTest {

    private val tokenRepository: TokenRepository = mockk()
    private val logger: KtorLogger = mockk()

    @Test
    fun `handleTokenRefresh success - returns new tokens and saves them`() = runTest {
        // Arrange
        // 1. Mock the dependencies
        coJustRun { tokenRepository.saveAccessToken(any()) }
        coJustRun { logger.error(any(), any(), any()) } // Allow error logging without failing the test

        // 2. Create a mock HTTP client that returns a successful response
        val mockEngine = MockEngine {
            val successResponseJson = """{"accessToken":"new-access-token","refreshToken":"new-refresh-token"}"""
            respond(
                content = successResponseJson,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
        }

        // Act
        // 3. Call the function under test
        val bearerTokens = handleTokenRefresh(
            basicClient = client,
            refreshToken = "old-refresh-token",
            refreshPath = "auth/refresh",
            tokenRepository = tokenRepository,
            logger = logger
        )

        // Assert
        // 4. Verify the results
        assertThat(bearerTokens).isNotNull()
        assertThat(bearerTokens?.accessToken).isEqualTo("new-access-token")

        // 5. Verify that the repository's save method was called
        coVerify(exactly = 1) { tokenRepository.saveAccessToken("new-access-token") }
    }

    @Test
    fun `handleTokenRefresh failure - returns null and clears repository`() = runTest {
        // Arrange
        // 1. Mock the dependencies
        coJustRun { tokenRepository.clear() }
        coJustRun { logger.error(any(), any(), any()) }

        // 2. Create a mock HTTP client that returns a server error
        val mockEngine = MockEngine {
            respond(
                content = "",
                status = HttpStatusCode.InternalServerError
            )
        }
        val client = HttpClient(mockEngine)

        // Act
        // 3. Call the function under test
        val bearerTokens = handleTokenRefresh(
            basicClient = client,
            refreshToken = "old-refresh-token",
            refreshPath = "auth/refresh",
            tokenRepository = tokenRepository,
            logger = logger
        )

        // Assert
        // 4. Verify the results
        assertThat(bearerTokens).isNull()

        // 5. Verify that the repository's clear method was called
        coVerify(exactly = 1) { tokenRepository.clear() }
    }
}
