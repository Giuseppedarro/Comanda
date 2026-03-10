package dev.giuseppedarro.comanda.features.login.repository

import dev.giuseppedarro.comanda.core.domain.model.DomainException
import dev.giuseppedarro.comanda.core.domain.repository.TokenRepository
import dev.giuseppedarro.comanda.features.login.data.repository.LoginRepositoryImpl
import dev.giuseppedarro.comanda.features.login.domain.model.LoginException
import dev.giuseppedarro.comanda.features.login.domain.repository.LoginRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.ByteReadChannel
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class LoginRepositoryImplTest {

    private val tokenRepository: TokenRepository = mockk(relaxed = true)

    @Test
    fun given_successful_response_then_save_tokens_and_return_success() = runBlocking {
        // Arrange
        val mockEngine = createMockEngineFor(
            status = HttpStatusCode.OK,
            jsonBody = """{"accessToken":"test_access_token","refreshToken":"test_refresh_token"}"""
        )
        val client = createMockClient(mockEngine)
        val repository: LoginRepository = LoginRepositoryImpl(client, tokenRepository)

        // Act
        val result = repository.login("id", "password")

        // Assert
        assertTrue(result.isSuccess)
        coVerify { tokenRepository.saveTokens("test_access_token", "test_refresh_token") }
    }

    @Test
    fun given_unauthorized_response_then_return_InvalidCredentials_exception() = runBlocking {
        // Arrange
        val mockEngine = createMockEngineFor(
            status = HttpStatusCode.Unauthorized,
            jsonBody = """{"error":"Invalid credentials"}"""
        )
        val client = createMockClient(mockEngine)
        val repository: LoginRepository = LoginRepositoryImpl(client, tokenRepository)

        // Act
        val result = repository.login("id", "wrong_password")

        // Assert
        assertTrue(result.isFailure)
        assertEquals(LoginException.InvalidCredentials, result.exceptionOrNull())
    }

    @Test
    fun given_network_exception_then_return_NetworkException() = runBlocking {
        // Arrange
        val mockEngine = MockEngine { throw IOException("No internet") }
        val client = createMockClient(mockEngine)
        val repository: LoginRepository = LoginRepositoryImpl(client, tokenRepository)

        // Act
        val result = repository.login("id", "password")

        // Assert
        assertTrue(result.isFailure)
        assertEquals(DomainException.NetworkException, result.exceptionOrNull())
    }

    private fun createMockClient(engine: MockEngine): HttpClient {
        return HttpClient(engine) {
            install(ContentNegotiation) {
                json()
            }
        }
    }

    private fun createMockEngineFor(status: HttpStatusCode, jsonBody: String): MockEngine {
        return MockEngine {
            respond(
                content = ByteReadChannel(jsonBody),
                status = status,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
    }
}
