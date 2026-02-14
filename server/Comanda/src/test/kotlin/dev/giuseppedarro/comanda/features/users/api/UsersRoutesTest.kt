package dev.giuseppedarro.comanda.features.users.api

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import dev.giuseppedarro.comanda.features.users.domain.model.User
import dev.giuseppedarro.comanda.features.users.domain.usecase.CreateUserUseCase
import dev.giuseppedarro.comanda.features.users.domain.usecase.DeleteUserUseCase
import dev.giuseppedarro.comanda.features.users.domain.usecase.GetUsersUseCase
import dev.giuseppedarro.comanda.features.users.domain.usecase.UpdateUserUseCase
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

    private val mySecret = "secret"
    private val myIssuer = "test-issuer"
    private val myAudience = "test-audience"

    private fun Application.setupTestApp(
        createUserUseCase: CreateUserUseCase = mockk(),
        getUsersUseCase: GetUsersUseCase = mockk(),
        updateUserUseCase: UpdateUserUseCase = mockk(),
        deleteUserUseCase: DeleteUserUseCase = mockk()
    ) {
        install(ContentNegotiation) {
            json()
        }
        
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
        
        install(Koin) {
            modules(module {
                single { createUserUseCase }
                single { getUsersUseCase }
                single { updateUserUseCase }
                single { deleteUserUseCase }
            })
        }
        
        this.routing {
            usersRoutes(createUserUseCase, getUsersUseCase, updateUserUseCase, deleteUserUseCase)
        }
    }

    private fun generateToken(): String {
        return JWT.create()
            .withAudience(myAudience)
            .withIssuer(myIssuer)
            .sign(Algorithm.HMAC256(mySecret))
    }

    @Test
    fun `POST users should return 201 Created when authorized`() = testApplication {
        val createUserUseCase = mockk<CreateUserUseCase>()
        val expectedUser = User(1, "john.doe", "John Doe", "waiter")
        coEvery { createUserUseCase(any()) } returns Result.success(expectedUser)

        application {
            setupTestApp(createUserUseCase = createUserUseCase)
        }

        val response = client.post("/users") {
            header(HttpHeaders.Authorization, "Bearer ${generateToken()}")
            contentType(ContentType.Application.Json)
            setBody("""{"employeeId": "john.doe", "name": "John Doe", "password": "pass", "role": "waiter"}""")
        }

        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun `GET users should return 200 OK`() = testApplication {
        val getUsersUseCase = mockk<GetUsersUseCase>()
        coEvery { getUsersUseCase() } returns Result.success(emptyList())

        application {
            setupTestApp(getUsersUseCase = getUsersUseCase)
        }

        val response = client.get("/users") {
            header(HttpHeaders.Authorization, "Bearer ${generateToken()}")
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `PUT users should return 200 OK when updating`() = testApplication {
        val updateUserUseCase = mockk<UpdateUserUseCase>()
        val expectedUser = User(1, "john.doe", "Updated Name", "waiter")
        coEvery { updateUserUseCase(any()) } returns Result.success(expectedUser)

        application {
            setupTestApp(updateUserUseCase = updateUserUseCase)
        }

        val response = client.put("/users/1") {
            header(HttpHeaders.Authorization, "Bearer ${generateToken()}")
            contentType(ContentType.Application.Json)
            setBody("""{"name": "Updated Name"}""")
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `DELETE users should return 204 No Content`() = testApplication {
        val deleteUserUseCase = mockk<DeleteUserUseCase>()
        coEvery { deleteUserUseCase(any()) } returns Result.success(Unit)

        application {
            setupTestApp(deleteUserUseCase = deleteUserUseCase)
        }

        val response = client.delete("/users/1") {
            header(HttpHeaders.Authorization, "Bearer ${generateToken()}")
        }

        assertEquals(HttpStatusCode.NoContent, response.status)
    }
}
