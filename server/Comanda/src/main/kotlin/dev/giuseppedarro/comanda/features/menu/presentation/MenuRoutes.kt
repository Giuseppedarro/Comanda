package dev.giuseppedarro.comanda.features.menu.presentation

import dev.giuseppedarro.comanda.features.menu.domain.usecase.GetMenuUseCase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.menuRoutes(getMenuUseCase: GetMenuUseCase) {
    authenticate("auth-jwt") {
        route("/menu") {
            get {
                getMenuUseCase()
                    .onSuccess { menu ->
                        call.respond(HttpStatusCode.OK, menu)
                    }
                    .onFailure {
                        call.respond(HttpStatusCode.InternalServerError, it.message ?: "Failed to fetch menu")
                    }
            }
        }
    }
}
