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
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class OrderRepositoryImpl(
    private val api: OrderApi,
    private val tokenRepository: TokenRepository
) : OrderRepository {

    // The full menu is needed to map the incoming order items to their full MenuItem objects.
    private val menuFlow = getMenu()

    override fun getMenu(): Flow<Result<List<MenuCategory>>> = flow {
        emit(Result.Loading())
        try {
            val token = tokenRepository.getAccessToken() ?: throw Exception("No auth token found")
            val menu = api.getMenu(token)
            emit(Result.Success(menu))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message ?: "An unknown error occurred", null))
        }
    }

    override fun getOrdersForTable(tableNumber: Int): Flow<Result<List<OrderItem>>> = flow {
        emit(Result.Loading())
        try {
            val token = tokenRepository.getAccessToken() ?: throw Exception("No auth token found")
            
            // Wait for the menu to be loaded, and handle if it fails
            val menuResult = menuFlow.first { it !is Result.Loading<*> } // Wait for first non-loading state
            if (menuResult is Result.Error) {
                emit(Result.Error("Menu could not be loaded, so order cannot be displayed.", null))
                return@flow
            }
            val allMenuItems = (menuResult as Result.Success).data.orEmpty().flatMap { it.items }

            // Now proceed with getting the orders for the table
            val orderResponses = api.getOrdersForTable(token, tableNumber)
            val orderForTable = orderResponses.lastOrNull()?.items ?: emptyList()

            val orderItems = orderForTable.mapNotNull { orderItemDto ->
                allMenuItems.find { it.name == orderItemDto.menuItemId }?.let {
                    OrderItem(menuItem = it, quantity = orderItemDto.quantity)
                }
            }

            emit(Result.Success(orderItems))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message ?: "An unknown error occurred", null))
        }
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
        } catch (e: ClientRequestException) {
            val response = e.response
            val text = response.bodyAsText()
            Result.Error("Error ${response.status.value}: $text")
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e.message ?: "An unknown error occurred")
        }
    }
}
