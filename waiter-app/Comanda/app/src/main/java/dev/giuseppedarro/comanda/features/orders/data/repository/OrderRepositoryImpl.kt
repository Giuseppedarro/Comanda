package dev.giuseppedarro.comanda.features.orders.data.repository

import dev.giuseppedarro.comanda.core.domain.TokenRepository
import dev.giuseppedarro.comanda.core.utils.Result
import dev.giuseppedarro.comanda.features.orders.data.remote.OrderApi
import dev.giuseppedarro.comanda.features.orders.data.remote.dto.OrderItemRequest
import dev.giuseppedarro.comanda.features.orders.data.remote.dto.SubmitOrderRequest
import dev.giuseppedarro.comanda.features.orders.domain.model.MenuCategory
import dev.giuseppedarro.comanda.features.orders.domain.model.MenuItem
import dev.giuseppedarro.comanda.features.orders.domain.model.OrderItem
import dev.giuseppedarro.comanda.features.orders.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class OrderRepositoryImpl(
    private val api: OrderApi,
    private val tokenRepository: TokenRepository
) : OrderRepository {

    private val mockMenu = listOf(
        MenuCategory("Appetizers", listOf(
            MenuItem("Bruschetta", "$7.00"),
            MenuItem("Garlic Bread", "$5.00"),
            MenuItem("Stuffed Mushrooms", "$8.50"),
            MenuItem("Spring Rolls", "$6.00"),
            MenuItem("Onion Rings", "$5.50"),
            MenuItem("Calamari", "$9.00")
        )),
        MenuCategory("Main Courses", listOf(MenuItem("Gourmet Burger", "$12.99"), MenuItem("Caesar Salad", "$8.50"))),
        MenuCategory("Desserts", listOf(MenuItem("Tiramisu", "$6.50"), MenuItem("Cheesecake", "$7.50"))),
        MenuCategory("Drinks", listOf(
            MenuItem("Cola", "$2.50"),
            MenuItem("Cappuccino", "$4.75"),
            MenuItem("Iced Tea", "$2.00"),
            MenuItem("Orange Juice", "$3.00"),
            MenuItem("Latte", "$4.00"),
            MenuItem("Water", "$1.00"),
            MenuItem("Espresso", "$3.00"),
            MenuItem("Lemonade", "$3.50"),
            MenuItem("Apple Juice", "$3.00"),
            MenuItem("Sparkling Water", "$1.50"),
            MenuItem("Green Tea", "$2.50"),
            MenuItem("Beer", "$5.00")
        ))
    )

    override fun getMenu(): Flow<Result<List<MenuCategory>>> = flow {
        emit(Result.Loading())
        emit(Result.Success(mockMenu))
    }

    override suspend fun submitOrder(tableNumber: Int, numberOfPeople: Int, items: List<OrderItem>): Result<Unit> {
        return try {
            val token = tokenRepository.getAccessToken() ?: throw Exception("No auth token found")

            val orderItemsRequest = items.map {
                OrderItemRequest(
                    menuItemId = it.menuItem.name, // Assuming name is the ID
                    quantity = it.quantity
                )
            }

            val request = SubmitOrderRequest(
                tableNumber = tableNumber,
                numberOfPeople = numberOfPeople,
                items = orderItemsRequest
            )

            api.submitOrder(token, request)
            Result.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e.message ?: "An unknown error occurred")
        }
    }
}
