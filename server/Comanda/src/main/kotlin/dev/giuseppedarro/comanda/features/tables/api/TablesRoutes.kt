package dev.giuseppedarro.comanda.features.tables.api

import dev.giuseppedarro.comanda.features.tables.domain.usecase.AddTableUseCase
import dev.giuseppedarro.comanda.features.tables.domain.usecase.GetTablesUseCase
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class AddTableRequest(val number: Int? = null)

@Serializable
data class TableResponse(val status: String, val message: String)

fun Route.tablesRoutes(getTablesUseCase: GetTablesUseCase, addTableUseCase: AddTableUseCase) {
    authenticate("auth-jwt") {
        route("/tables") {
            get {
                val tables = getTablesUseCase()
                call.respond(HttpStatusCode.OK, tables)
            }

            post {
                val request = try { call.receive<AddTableRequest>() } catch (e: Exception) { AddTableRequest() }

                if (request.number != null && request.number < 1) {
                    call.respond(HttpStatusCode.BadRequest, TableResponse("error", "Table number must be a positive integer"))
                    return@post
                }

                val result = addTableUseCase(request.number)
                result.onSuccess { table ->
                    call.respond(HttpStatusCode.Created, table)
                }.onFailure { error ->
                    call.respond(HttpStatusCode.Conflict, TableResponse("error", error.message ?: "A table with that number already exists"))
                }
            }
        }
    }
}
