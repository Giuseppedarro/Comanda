package dev.giuseppedarro.comanda.features.tables.api

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import dev.giuseppedarro.comanda.features.tables.domain.model.Table
import dev.giuseppedarro.comanda.features.tables.domain.usecase.AddTableUseCase
import dev.giuseppedarro.comanda.features.tables.domain.usecase.GetTablesUseCase
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

class TablesRoutesTest {

    private val mySecret = "secret"
    private val myIssuer = "test-issuer"
    private val myAudience = "test-audience"

    private fun Application.setupTestApp(
        getTablesUseCase: GetTablesUseCase = mockk(),
        addTableUseCase: AddTableUseCase = mockk()
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
                single { getTablesUseCase }
                single { addTableUseCase }
            })
        }

        this.routing {
            tablesRoutes(getTablesUseCase, addTableUseCase)
        }
    }

    private fun generateToken(): String {
        return JWT.create()
            .withAudience(myAudience)
            .withIssuer(myIssuer)
            .withClaim("role", "ADMIN")
            .sign(Algorithm.HMAC256(mySecret))
    }

    @Test
    fun `GET tables should return 200 OK with list of tables`() = testApplication {
        val getTablesUseCase = mockk<GetTablesUseCase>()
        coEvery { getTablesUseCase() } returns listOf(Table(1, false), Table(2, true))

        application { setupTestApp(getTablesUseCase = getTablesUseCase) }

        val response = client.get("/tables") {
            header(HttpHeaders.Authorization, "Bearer ${generateToken()}")
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `POST tables should return 201 Created when table number is available`() = testApplication {
        val addTableUseCase = mockk<AddTableUseCase>()
        val expectedTable = Table(number = 5, isOccupied = false)
        coEvery { addTableUseCase(5) } returns Result.success(expectedTable)

        application { setupTestApp(addTableUseCase = addTableUseCase) }

        val response = client.post("/tables") {
            header(HttpHeaders.Authorization, "Bearer ${generateToken()}")
            contentType(ContentType.Application.Json)
            setBody("""{"number": 5}""")
        }

        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun `POST tables should return 201 Created with auto-increment when no number is provided`() = testApplication {
        val addTableUseCase = mockk<AddTableUseCase>()
        val expectedTable = Table(number = 6, isOccupied = false)
        coEvery { addTableUseCase(null) } returns Result.success(expectedTable)

        application { setupTestApp(addTableUseCase = addTableUseCase) }

        val response = client.post("/tables") {
            header(HttpHeaders.Authorization, "Bearer ${generateToken()}")
            contentType(ContentType.Application.Json)
            setBody("""{}""")
        }

        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun `POST tables should return 409 Conflict when table number already exists`() = testApplication {
        val addTableUseCase = mockk<AddTableUseCase>()
        coEvery { addTableUseCase(3) } returns Result.failure(IllegalArgumentException("Table with number 3 already exists"))

        application { setupTestApp(addTableUseCase = addTableUseCase) }

        val response = client.post("/tables") {
            header(HttpHeaders.Authorization, "Bearer ${generateToken()}")
            contentType(ContentType.Application.Json)
            setBody("""{"number": 3}""")
        }

        assertEquals(HttpStatusCode.Conflict, response.status)
    }

    @Test
    fun `POST tables should return 400 Bad Request when number is less than 1`() = testApplication {
        application { setupTestApp() }

        val response = client.post("/tables") {
            header(HttpHeaders.Authorization, "Bearer ${generateToken()}")
            contentType(ContentType.Application.Json)
            setBody("""{"number": 0}""")
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `POST tables should return 401 Unauthorized when no token provided`() = testApplication {
        application { setupTestApp() }

        val response = client.post("/tables") {
            contentType(ContentType.Application.Json)
            setBody("""{"number": 5}""")
        }

        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }
}

