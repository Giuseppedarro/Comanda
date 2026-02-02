package dev.giuseppedarro.comanda.features.printers.api

import dev.giuseppedarro.comanda.features.printers.data.model.CreatePrinterRequest
import dev.giuseppedarro.comanda.features.printers.data.model.UpdatePrinterRequest
import dev.giuseppedarro.comanda.features.printers.domain.usecase.CreatePrinterUseCase
import dev.giuseppedarro.comanda.features.printers.domain.usecase.DeletePrinterUseCase
import dev.giuseppedarro.comanda.features.printers.domain.usecase.GetAllPrintersUseCase
import dev.giuseppedarro.comanda.features.printers.domain.usecase.UpdatePrinterUseCase
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class PrinterResponse(val status: String, val message: String)

fun Route.printersRoutes(
    getAllPrintersUseCase: GetAllPrintersUseCase,
    createPrinterUseCase: CreatePrinterUseCase,
    updatePrinterUseCase: UpdatePrinterUseCase,
    deletePrinterUseCase: DeletePrinterUseCase
) {
    authenticate("auth-jwt") {
        route("/printers") {
            // GET /printers - Get all printers
            get {
                val printers = getAllPrintersUseCase()
                call.respond(HttpStatusCode.OK, printers)
            }

            // POST /printers - Create new printer
            post {
                val request = call.receive<CreatePrinterRequest>()

                val validationError = when {
                    request.name.isBlank() -> "name must not be blank"
                    request.address.isBlank() -> "address must not be blank"
                    request.port < 1 || request.port > 65535 -> "port must be between 1 and 65535"
                    else -> null
                }

                if (validationError != null) {
                    call.respond(HttpStatusCode.BadRequest, PrinterResponse("error", validationError))
                    return@post
                }

                val printer = createPrinterUseCase(request.name, request.address, request.port)
                call.respond(HttpStatusCode.Created, printer)
            }

            // PUT /printers/{id} - Update printer
            put("/{id}") {
                val idParam = call.parameters["id"]
                if (idParam.isNullOrBlank()) {
                    call.respond(HttpStatusCode.BadRequest, PrinterResponse("error", "printer id is required"))
                    return@put
                }

                val id = idParam.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, PrinterResponse("error", "invalid printer id"))
                    return@put
                }

                val request = call.receive<UpdatePrinterRequest>()

                val validationError = when {
                    request.name.isBlank() -> "name must not be blank"
                    request.address.isBlank() -> "address must not be blank"
                    request.port < 1 || request.port > 65535 -> "port must be between 1 and 65535"
                    else -> null
                }

                if (validationError != null) {
                    call.respond(HttpStatusCode.BadRequest, PrinterResponse("error", validationError))
                    return@put
                }

                val updatedPrinter = updatePrinterUseCase(id, request.name, request.address, request.port)
                if (updatedPrinter != null) {
                    call.respond(HttpStatusCode.OK, updatedPrinter)
                } else {
                    call.respond(HttpStatusCode.NotFound, PrinterResponse("error", "Printer not found"))
                }
            }

            // DELETE /printers/{id} - Delete printer
            delete("/{id}") {
                val idParam = call.parameters["id"]
                if (idParam.isNullOrBlank()) {
                    call.respond(HttpStatusCode.BadRequest, PrinterResponse("error", "printer id is required"))
                    return@delete
                }

                val id = idParam.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, PrinterResponse("error", "invalid printer id"))
                    return@delete
                }

                val success = deletePrinterUseCase(id)
                if (success) {
                    call.respond(HttpStatusCode.OK, PrinterResponse("success", "Printer deleted successfully"))
                } else {
                    call.respond(HttpStatusCode.NotFound, PrinterResponse("error", "Printer not found"))
                }
            }
        }
    }
}