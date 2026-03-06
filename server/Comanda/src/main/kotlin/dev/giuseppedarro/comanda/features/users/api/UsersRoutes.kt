package dev.giuseppedarro.comanda.features.users.api

import dev.giuseppedarro.comanda.features.users.domain.usecase.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
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
data class UpdateUserRequest(
    val employeeId: String? = null,
    val name: String? = null,
    val password: String? = null,
    val role: String? = null
)

@Serializable
data class UserResponse(
    val id: Int,
    val employeeId: String,
    val name: String,
    val role: String
)

fun Route.usersRoutes(
    createUserUseCase: CreateUserUseCase,
    getUsersUseCase: GetUsersUseCase,
    getUserByIdUseCase: GetUserByIdUseCase,
    updateUserUseCase: UpdateUserUseCase,
    deleteUserUseCase: DeleteUserUseCase
) {
    authenticate("auth-jwt") {
        route("/users") {
            post {
                val principal = call.principal<JWTPrincipal>()
                val role = principal?.payload?.getClaim("role")?.asString()
                val userId = principal?.payload?.getClaim("userId")?.asString()

                println("POST /users request from userId: $userId, role: $role")

                if (role != "ADMIN") {
                    println("Access denied for userId: $userId with role: $role")
                    call.respond(HttpStatusCode.Forbidden, mapOf("error" to "Only admins can create users"))
                    return@post
                }

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

            get {
                val result = getUsersUseCase()
                result.onSuccess { users ->
                    val response = users.map { user ->
                        UserResponse(user.id, user.employeeId, user.name, user.role)
                    }
                    call.respond(HttpStatusCode.OK, response)
                }.onFailure { error ->
                    call.respond(HttpStatusCode.InternalServerError, mapOf("error" to (error.message ?: "Unknown error")))
                }
            }

            get("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid user ID"))
                    return@get
                }

                val result = getUserByIdUseCase(id)
                result.onSuccess { user ->
                    call.respond(HttpStatusCode.OK, UserResponse(user.id, user.employeeId, user.name, user.role))
                }.onFailure { error ->
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to (error.message ?: "User not found")))
                }
            }

            put("/{id}") {
                val principal = call.principal<JWTPrincipal>()
                val role = principal?.payload?.getClaim("role")?.asString()
                val userId = principal?.payload?.getClaim("userId")?.asString()

                println("PUT /users/{id} request from userId: $userId, role: $role")

                if (role != "ADMIN") {
                    println("Access denied for userId: $userId with role: $role")
                    call.respond(HttpStatusCode.Forbidden, mapOf("error" to "Only admins can update users"))
                    return@put
                }

                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid user ID"))
                    return@put
                }

                val request = call.receive<UpdateUserRequest>()
                val params = UpdateUserParams(
                    id = id,
                    employeeId = request.employeeId,
                    name = request.name,
                    password = request.password,
                    role = request.role
                )

                val result = updateUserUseCase(params)
                result.onSuccess { user ->
                    call.respond(HttpStatusCode.OK, UserResponse(user.id, user.employeeId, user.name, user.role))
                }.onFailure { error ->
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to (error.message ?: "Unknown error")))
                }
            }

            delete("/{id}") {
                val principal = call.principal<JWTPrincipal>()
                val role = principal?.payload?.getClaim("role")?.asString()
                val userId = principal?.payload?.getClaim("userId")?.asString()

                println("DELETE /users/{id} request from userId: $userId, role: $role")

                if (role != "ADMIN") {
                    println("Access denied for userId: $userId with role: $role")
                    call.respond(HttpStatusCode.Forbidden, mapOf("error" to "Only admins can delete users"))
                    return@delete
                }

                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid user ID"))
                    return@delete
                }

                val result = deleteUserUseCase(id)
                result.onSuccess {
                    call.respond(HttpStatusCode.NoContent)
                }.onFailure { error ->
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to (error.message ?: "Unknown error")))
                }
            }
        }
    }
}
