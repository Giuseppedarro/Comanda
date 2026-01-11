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

@Serializable
data class MenuItemDto(val name: String, val price: String)

@Serializable
data class MenuCategoryDto(val name: String, val items: List<MenuItemDto>)

fun Route.ordersRoutes(
    submitOrderUseCase: SubmitOrderUseCase,
    getOrdersUseCase: GetOrdersUseCase,
    getOrdersForTableUseCase: GetOrdersForTableUseCase,
    getOrderByIdUseCase: GetOrderByIdUseCase,
) {
    authenticate("auth-jwt") {
        route("/orders") {
            // GET /orders/menu - returns the restaurant menu (hard-coded for now)
            get("/menu") {
                val menu = listOf(
                    MenuCategoryDto(
                        name = "Appetizers",
                        items = listOf(
                            MenuItemDto("Bruschetta", "$7.00"),
                            MenuItemDto("Garlic Bread", "$5.00"),
                            MenuItemDto("Stuffed Mushrooms", "$8.50"),
                            MenuItemDto("Spring Rolls", "$6.00"),
                            MenuItemDto("Onion Rings", "$5.50"),
                            MenuItemDto("Calamari", "$9.00")
                        )
                    ),
                    MenuCategoryDto(
                        name = "Main Courses",
                        items = listOf(
                            MenuItemDto("Gourmet Burger", "$12.99"),
                            MenuItemDto("Caesar Salad", "$8.50")
                        )
                    ),
                    MenuCategoryDto(
                        name = "Desserts",
                        items = listOf(
                            MenuItemDto("Tiramisu", "$6.50"),
                            MenuItemDto("Cheesecake", "$7.50")
                        )
                    ),
                    MenuCategoryDto(
                        name = "Drinks",
                        items = listOf(
                            MenuItemDto("Cola", "$2.50"),
                            MenuItemDto("Cappuccino", "$4.75"),
                            MenuItemDto("Iced Tea", "$2.00"),
                            MenuItemDto("Orange Juice", "$3.00"),
                            MenuItemDto("Latte", "$4.00"),
                            MenuItemDto("Water", "$1.00"),
                            MenuItemDto("Espresso", "$3.00"),
                            MenuItemDto("Lemonade", "$3.50"),
                            MenuItemDto("Apple Juice", "$3.00"),
                            MenuItemDto("Sparkling Water", "$1.50"),
                            MenuItemDto("Green Tea", "$2.50"),
                            MenuItemDto("Beer", "$5.00")
                        )
                    )
                )
                call.respond(HttpStatusCode.OK, menu)
            }

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
                    val location = "/orders/" + created.id
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

            // GET /orders/{orderId}
            get("/{orderId}") {
                val orderId = call.parameters["orderId"]
                if (orderId.isNullOrBlank()) {
                    call.respond(HttpStatusCode.BadRequest, OrderResponse("error", "orderId is required"))
                    return@get
                }
                val order = getOrderByIdUseCase(orderId)
                if (order == null) {
                    call.respond(HttpStatusCode.NotFound, OrderResponse("error", "Order not found"))
                } else {
                    call.respond(HttpStatusCode.OK, order)
                }
            }
        }
    }
}
