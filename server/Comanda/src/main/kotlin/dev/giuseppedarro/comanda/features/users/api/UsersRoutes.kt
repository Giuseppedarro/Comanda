package dev.giuseppedarro.comanda.features.users.api

import dev.giuseppedarro.comanda.features.users.domain.usecase.CreateUserParams
import dev.giuseppedarro.comanda.features.users.domain.usecase.CreateUserUseCase
import dev.giuseppedarro.comanda.features.users.domain.usecase.DeleteUserUseCase
import dev.giuseppedarro.comanda.features.users.domain.usecase.GetUsersUseCase
import dev.giuseppedarro.comanda.features.users.domain.usecase.UpdateUserParams
import dev.giuseppedarro.comanda.features.users.domain.usecase.UpdateUserUseCase
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
    updateUserUseCase: UpdateUserUseCase,
    deleteUserUseCase: DeleteUserUseCase
) {
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

            put("/{id}") {
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
