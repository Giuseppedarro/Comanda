package dev.giuseppedarro.comanda.features.payments.presentation

import dev.giuseppedarro.comanda.features.payments.data.model.PaymentRequest
import dev.giuseppedarro.comanda.features.payments.domain.usecase.GetBillUseCase
import dev.giuseppedarro.comanda.features.payments.domain.usecase.ProcessPaymentUseCase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.paymentsRoutes(
    getBillUseCase: GetBillUseCase,
    processPaymentUseCase: ProcessPaymentUseCase
) {
    authenticate("auth-jwt") {
        route("/payments") {
            get("/bill/{tableNumber}") {
                val tableNumber = call.parameters["tableNumber"]?.toIntOrNull()
                if (tableNumber == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid table number")
                    return@get
                }

                getBillUseCase(tableNumber)
                    .onSuccess { bill ->
                        call.respond(HttpStatusCode.OK, bill)
                    }
                    .onFailure {
                        call.respond(HttpStatusCode.NotFound, it.message ?: "Order not found")
                    }
            }

            post("/pay") {
                val request = call.receive<PaymentRequest>()
                processPaymentUseCase(request)
                    .onSuccess {
                        call.respond(HttpStatusCode.OK, "Payment processed successfully")
                    }
                    .onFailure {
                        call.respond(HttpStatusCode.BadRequest, it.message ?: "Payment failed")
                    }
            }
        }
    }
}
