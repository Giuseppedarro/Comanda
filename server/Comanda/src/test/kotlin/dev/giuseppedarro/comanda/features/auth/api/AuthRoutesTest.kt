package dev.giuseppedarro.comanda.features.auth.api

import dev.giuseppedarro.comanda.features.auth.data.model.LoginRequest
import dev.giuseppedarro.comanda.features.auth.data.model.LoginResponse
import dev.giuseppedarro.comanda.features.auth.domain.usecase.LoginUseCase
import dev.giuseppedarro.comanda.features.auth.domain.usecase.RefreshTokenUseCase
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class AuthRoutesTest {

    @Test
    fun `POST auth login should return 200 OK with valid credentials`() = testApplication {
        val loginUseCase = mockk<LoginUseCase>()
        val refreshTokenUseCase = mockk<RefreshTokenUseCase>()
        
        val loginResponse = LoginResponse("access-token", "refresh-token")
        coEvery { loginUseCase(any()) } returns Result.success(loginResponse)

        application {
            install(ContentNegotiation) {
                json()
            }
            routing {
                authRoutes(loginUseCase, refreshTokenUseCase)
            }
        }

        val response = client.post("/auth/login") {
            contentType(ContentType.Application.Json)
            setBody("""
                {
                    "employeeId": "1234",
                    "password": "password"
                }
            """.trimIndent())
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }
}
