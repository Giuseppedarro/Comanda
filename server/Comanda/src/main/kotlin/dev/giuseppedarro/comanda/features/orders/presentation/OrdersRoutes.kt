package dev.giuseppedarro.comanda.features.orders.presentation

import dev.giuseppedarro.comanda.features.orders.data.model.SubmitOrderRequest
import dev.giuseppedarro.comanda.features.orders.domain.usecase.GetOrderByIdUseCase
import dev.giuseppedarro.comanda.features.orders.domain.usecase.GetOrdersForTableUseCase
import dev.giuseppedarro.comanda.features.orders.domain.usecase.GetOrdersUseCase
import dev.giuseppedarro.comanda.features.orders.domain.usecase.SubmitOrderUseCase
import dev.giuseppedarro.comanda.features.orders.domain.model.Order
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class OrderResponse(val status: String, val message: String)

fun Route.ordersRoutes(
    submitOrderUseCase: SubmitOrderUseCase,
    getOrdersUseCase: GetOrdersUseCase,
    getOrdersForTableUseCase: GetOrdersForTableUseCase,
    getOrderByIdUseCase: GetOrderByIdUseCase,
) {
    authenticate("auth-jwt") {
        route("/orders") {
            post {
                val request = call.receive<SubmitOrderRequest>()
                println(request)
                // Basic validation
                val validationError = when {
                    request.tableNumber < 1 -> "tableNumber must be >= 1"
                    request.numberOfPeople < 1 -> "numberOfPeople must be >= 1"
                    request.items.isEmpty() -> "items must not be empty"
                    request.items.any { it.menuItemId.isBlank() } -> "each item.menuItemId is required"
                    request.items.any { it.quantity < 1 } -> "each item.quantity must be >= 1"
                    else -> null
                }

                if (validationError != null) {
                    call.respond(HttpStatusCode.BadRequest, OrderResponse("error", validationError))
                    return@post
                }

                val result = submitOrderUseCase(request)
                val created = result.getOrNull()
                if (created is Order) {
                    val location = "/orders/" + created.tableNumber
                    call.response.headers.append(HttpHeaders.Location, location)
                    call.respond(HttpStatusCode.Created, created)
                } else {
                    call.respond(
                        HttpStatusCode.InternalServerError,
                        OrderResponse("error", result.exceptionOrNull()?.message ?: "An unknown error occurred")
                    )
                }
            }

            // GET /orders and optional filter: ?tableNumber=12
            get {
                val tableParam = call.request.queryParameters["tableNumber"]
                val tableNumber = tableParam?.toIntOrNull()
                if (tableParam != null && tableNumber == null) {
                    call.respond(HttpStatusCode.BadRequest, OrderResponse("error", "Invalid tableNumber"))
                    return@get
                }
                val orders = if (tableNumber != null) {
                    getOrdersForTableUseCase(tableNumber)
                } else {
                    getOrdersUseCase()
                }
                call.respond(HttpStatusCode.OK, orders)
            }

            // GET /orders/{tableNumber}
            get("/{tableNumber}") {
                val tableParam = call.parameters["tableNumber"]
                if (tableParam.isNullOrBlank()) {
                    call.respond(HttpStatusCode.BadRequest, OrderResponse("error", "tableNumber is required"))
                    return@get
                }
                val tableNumber = tableParam.toIntOrNull()
                if (tableNumber == null) {
                    call.respond(HttpStatusCode.BadRequest, OrderResponse("error", "Invalid tableNumber"))
                    return@get
                }
                val order = getOrderByIdUseCase(tableNumber.toString())
                if (order == null) {
                    call.respond(HttpStatusCode.NotFound, OrderResponse("error", "Order not found"))
                } else {
                    call.respond(HttpStatusCode.OK, order)
                }
            }
        }
    }
}
