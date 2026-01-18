package dev.giuseppedarro.comanda.features.menu.presentation

import dev.giuseppedarro.comanda.features.menu.domain.usecase.GetMenuUseCase
import dev.giuseppedarro.comanda.features.menu.domain.usecase.UpdateMenuUseCase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class MenuResponse(val status: String, val message: String)

fun Route.menuRoutes(
    getMenuUseCase: GetMenuUseCase,
    updateMenuUseCase: UpdateMenuUseCase
) {
    authenticate("auth-jwt") {
        route("/menu") {
            get {
                val menu = getMenuUseCase()
                val menuDto = menu.map { it.toDto() }
                call.respond(HttpStatusCode.OK, menuDto)
            }

            post {
                val request = call.receive<MenuUpdateRequest>()

                // Basic validation
                val validationError = when {
                    request.categories.isEmpty() -> "Menu must contain at least one category"
                    else -> null
                }

                if (validationError != null) {
                    call.respond(HttpStatusCode.BadRequest, MenuResponse("error", validationError))
                    return@post
                }

                val result = updateMenuUseCase(request.categories)

                result.fold(
                    onSuccess = {
                        call.respond(HttpStatusCode.OK, MenuResponse("success", "Menu updated successfully"))
                    },
                    onFailure = { exception ->
                        val status = when (exception) {
                            is IllegalArgumentException -> HttpStatusCode.BadRequest
                            else -> HttpStatusCode.InternalServerError
                        }
                        call.respond(
                            status,
                            MenuResponse("error", exception.message ?: "An unknown error occurred")
                        )
                    }
                )
            }
        }
    }
}