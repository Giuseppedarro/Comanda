package dev.giuseppedarro.comanda.features.tables.presentation

import dev.giuseppedarro.comanda.features.tables.domain.usecase.AddTableUseCase
import dev.giuseppedarro.comanda.features.tables.domain.usecase.GetTablesUseCase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.tablesRoutes(getTablesUseCase: GetTablesUseCase, addTableUseCase: AddTableUseCase) {
    authenticate("auth-jwt") {
        route("/tables") {
            get {
                val tables = getTablesUseCase()
                call.respond(HttpStatusCode.OK, tables)
            }

            post {
                val newTable = addTableUseCase()
                call.respond(HttpStatusCode.Created, newTable)
            }
        }
    }
}
