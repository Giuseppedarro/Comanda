package dev.giuseppedarro.comanda.features.orders.presentation

import dev.giuseppedarro.comanda.features.orders.data.model.SubmitOrderRequest
import dev.giuseppedarro.comanda.features.orders.domain.usecase.GetOrdersForTableUseCase
import dev.giuseppedarro.comanda.features.orders.domain.usecase.GetOrdersUseCase
import dev.giuseppedarro.comanda.features.orders.domain.usecase.SubmitOrderUseCase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class OrderResponse(val status: String, val message: String)

fun Route.ordersRoutes(submitOrderUseCase: SubmitOrderUseCase, getOrdersUseCase: GetOrdersUseCase, getOrdersForTableUseCase: GetOrdersForTableUseCase) {
    authenticate("auth-jwt") {
        route("/orders") {
            post {
                val request = call.receive<SubmitOrderRequest>()
                submitOrderUseCase(request)
                    .onSuccess {
                        call.respond(
                            HttpStatusCode.Created,
                            OrderResponse("success", "Order received for table ${request.tableNumber}")
                        )
                    }
                    .onFailure {
                        call.respond(
                            HttpStatusCode.InternalServerError,
                            OrderResponse("error", it.message ?: "An unknown error occurred")
                        )
                    }
            }

            get {
                val orders = getOrdersUseCase()
                call.respond(HttpStatusCode.OK, orders)
            }

            get("/{tableNumber}") {
                val tableNumber = call.parameters["tableNumber"]?.toIntOrNull()
                if (tableNumber == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid table number")
                    return@get
                }
                val orders = getOrdersForTableUseCase(tableNumber)
                call.respond(HttpStatusCode.OK, orders)
            }
        }
    }
}
