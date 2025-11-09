package dev.giuseppedarro.comanda.features.tables.presentation

import dev.giuseppedarro.comanda.features.tables.domain.usecase.GetTablesUseCase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.tablesRoutes(getTablesUseCase: GetTablesUseCase) {
    authenticate("auth-jwt") {
        get("/tables") {
            println("get tables called")
            val tables = getTablesUseCase()
            call.respond(HttpStatusCode.OK, tables)
        }
    }
}
