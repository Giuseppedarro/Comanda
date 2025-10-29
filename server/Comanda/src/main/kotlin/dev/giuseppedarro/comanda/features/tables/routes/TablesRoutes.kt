package dev.giuseppedarro.comanda.features.tables.routes

import dev.giuseppedarro.comanda.features.tables.data.Table
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.tablesRoutes() {
    authenticate("auth-jwt") { // <-- Correctly referencing the named provider
        get("/tables") {
            println("get /tables is called")
            val mockTables = List(10) { Table(number = it + 1, isOccupied = it % 2 == 0) }
            call.respond(HttpStatusCode.OK, mockTables)
        }
    }
}
