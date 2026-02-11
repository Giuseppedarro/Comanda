package dev.giuseppedarro.comanda.features.users.api

import dev.giuseppedarro.comanda.features.users.domain.usecase.CreateUserParams
import dev.giuseppedarro.comanda.features.users.domain.usecase.CreateUserUseCase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class CreateUserRequest(
    val employeeId: String,
    val name: String,
    val password: String,
    val role: String
)

@Serializable
data class UserResponse(
    val id: Int,
    val employeeId: String,
    val name: String,
    val role: String
)

fun Route.usersRoutes(createUserUseCase: CreateUserUseCase) {
    authenticate("auth-jwt") {
        route("/users") {
            post {
                val request = call.receive<CreateUserRequest>()
                
                val params = CreateUserParams(
                    employeeId = request.employeeId,
                    name = request.name,
                    password = request.password,
                    role = request.role
                )

                val result = createUserUseCase(params)
                
                result.onSuccess { user ->
                    call.respond(HttpStatusCode.Created, UserResponse(user.id, user.employeeId, user.name, user.role))
                }.onFailure { error ->
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to (error.message ?: "Unknown error")))
                }
            }
        }
    }
}
