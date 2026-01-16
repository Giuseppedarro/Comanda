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
                val menu = getMenuUseCase()
                val menuDto = menu.map { it.toDto() }
                call.respond(HttpStatusCode.OK, menuDto)
            }
        }
    }
}