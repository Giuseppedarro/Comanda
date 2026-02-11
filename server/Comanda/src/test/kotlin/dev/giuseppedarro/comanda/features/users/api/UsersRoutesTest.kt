package dev.giuseppedarro.comanda.features.users.api

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import dev.giuseppedarro.comanda.features.users.domain.model.User
import dev.giuseppedarro.comanda.features.users.domain.usecase.CreateUserUseCase
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

class UsersRoutesTest {

    @Test
    fun `POST users should return 201 Created when authorized and valid data`() = testApplication {
        // Mock UseCase
        val createUserUseCase = mockk<CreateUserUseCase>()
        val expectedUser = User(1, "john.doe", "John Doe", "waiter")
        
        coEvery { createUserUseCase(any()) } returns Result.success(expectedUser)

        // Setup Application
        application {
            install(ContentNegotiation) {
                json()
            }
            
            // Setup Auth with a real verifier for a dummy secret
            val mySecret = "secret"
            val myIssuer = "test-issuer"
            val myAudience = "test-audience"
            
            install(Authentication) {
                jwt("auth-jwt") {
                    verifier(
                        JWT.require(Algorithm.HMAC256(mySecret))
                            .withAudience(myAudience)
                            .withIssuer(myIssuer)
                            .build()
                    )
                    validate { credential ->
                        if (credential.payload.audience.contains(myAudience)) JWTPrincipal(credential.payload) else null
                    }
                }
            }
            
            // Setup Koin with mock
            install(Koin) {
                modules(module {
                    single { createUserUseCase }
                })
            }
            
            // Install Routes - Explicitly call routing on the application
            this.routing {
                usersRoutes(createUserUseCase)
            }
        }

        // Generate a valid token
        val token = JWT.create()
            .withAudience("test-audience")
            .withIssuer("test-issuer")
            .sign(Algorithm.HMAC256("secret"))

        // Execute Request
        val response = client.post("/users") {
            header(HttpHeaders.Authorization, "Bearer $token")
            contentType(ContentType.Application.Json)
            setBody("""
                {
                    "employeeId": "john.doe",
                    "name": "John Doe",
                    "password": "password123",
                    "role": "waiter"
                }
            """.trimIndent())
        }

        // Assert
        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun `POST users should return 401 Unauthorized when no token`() = testApplication {
        val createUserUseCase = mockk<CreateUserUseCase>()
        
        application {
            install(ContentNegotiation) { json() }
            install(Authentication) {
                jwt("auth-jwt") {
                    validate { null } // Fail validation
                }
            }
            this.routing {
                usersRoutes(createUserUseCase)
            }
        }

        val response = client.post("/users") {
            contentType(ContentType.Application.Json)
            setBody("""{"employeeId": "test"}""")
        }

        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }
}
