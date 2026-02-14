package dev.giuseppedarro.comanda.features.orders.data.repository

import dev.giuseppedarro.comanda.features.orders.data.remote.OrderApi
import dev.giuseppedarro.comanda.features.orders.data.remote.dto.OrderItemRequest
import dev.giuseppedarro.comanda.features.orders.data.remote.dto.SubmitOrderRequest
import dev.giuseppedarro.comanda.features.orders.domain.model.MenuCategory
import dev.giuseppedarro.comanda.features.orders.domain.model.MenuItem
import dev.giuseppedarro.comanda.features.orders.domain.model.Order
import dev.giuseppedarro.comanda.features.orders.domain.model.OrderItem
import dev.giuseppedarro.comanda.features.orders.domain.model.OrderStatus
import dev.giuseppedarro.comanda.features.orders.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class OrderRepositoryImpl(
    private val api: OrderApi
) : OrderRepository {

    override fun getMenu(): Flow<Result<List<MenuCategory>>> = flow {
        try {
            val remoteMenu = api.getMenu()
            val mapped = remoteMenu.map { categoryDto ->
                MenuCategory(
                    name = categoryDto.name,
                    items = categoryDto.items.map { itemDto ->
                        MenuItem(
                            id = itemDto.id ?: itemDto.name,
                            name = itemDto.name,
                            price = itemDto.price
                        )
                    }
                )
            }
            emit(Result.success(mapped))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override fun getOrdersForTable(tableNumber: Int): Flow<Result<Order?>> = flow {
        try {
            // Token is automatically injected by Ktor Auth plugin
            val orderResponse = api.getOrdersForTable(tableNumber)

            // If orderResponse is null, it means no order exists for this table (404 from backend)
            if (orderResponse == null) {
                emit(Result.success(null))
                return@flow
            }

            // We need the full menu to find the price and other details of the menu items.
            // Fetch the menu fresh to avoid race conditions with the menu flow.
            val remoteMenu = api.getMenu()
            val allMenuItems = remoteMenu.flatMap { categoryDto ->
                categoryDto.items.map { itemDto ->
                    MenuItem(
                        id = itemDto.id ?: itemDto.name,
                        name = itemDto.name,
                        price = itemDto.price
                    )
                }
            }

            // Map the DTO items to domain OrderItems
            val orderItems = orderResponse.items.mapNotNull { orderItemDto ->
                allMenuItems.find { it.id == orderItemDto.itemId }?.let { menuItem ->
                    OrderItem(
                        menuItem = menuItem,
                        quantity = orderItemDto.quantity,
                        orderItemId = orderItemDto.orderItemId,
                        notes = orderItemDto.notes
                    )
                }
            }

            // Map the full order response to domain Order
            val order = Order(
                tableNumber = orderResponse.tableNumber,
                numberOfPeople = orderResponse.numberOfPeople,
                status = OrderStatus.fromString(orderResponse.status),
                items = orderItems,
                createdAt = orderResponse.createdAt,
                subtotal = orderResponse.subtotal,
                serviceCharge = orderResponse.serviceCharge,
                total = orderResponse.total
            )

            emit(Result.success(order))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override suspend fun submitOrder(tableNumber: Int, numberOfPeople: Int, items: List<OrderItem>): Result<Unit> {
        return try {
            val orderItemsRequest = items.map {
                OrderItemRequest(
                    menuItemId = it.menuItem.id,
                    quantity = it.quantity,
                    notes = it.notes
                )
            }

            val request = SubmitOrderRequest(
                tableNumber = tableNumber,
                numberOfPeople = numberOfPeople,
                items = orderItemsRequest
            )

            api.submitOrder(request)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun printBill(tableNumber: Int, numberOfPeople: Int, items: List<OrderItem>): Result<Unit> {
        return try {
            val orderItemsRequest = items.map {
                OrderItemRequest(
                    menuItemId = it.menuItem.id,
                    quantity = it.quantity,
                    notes = it.notes
                )
            }

            val request = SubmitOrderRequest(
                tableNumber = tableNumber,
                numberOfPeople = numberOfPeople,
                items = orderItemsRequest
            )

            api.printBill(request)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}