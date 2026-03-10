package dev.giuseppedarro.comanda.features.orders.data.repository

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.giuseppedarro.comanda.core.network.MenuApi
import dev.giuseppedarro.comanda.core.network.dto.MenuCategoryDto
import dev.giuseppedarro.comanda.core.network.dto.MenuItemDto
import dev.giuseppedarro.comanda.features.orders.data.remote.OrderApi
import dev.giuseppedarro.comanda.features.orders.data.remote.dto.GetOrderResponse
import dev.giuseppedarro.comanda.features.orders.data.remote.dto.OrderResponseItem
import dev.giuseppedarro.comanda.features.orders.domain.model.MenuItem
import dev.giuseppedarro.comanda.features.orders.domain.model.OrderException
import dev.giuseppedarro.comanda.features.orders.domain.model.OrderItem
import dev.giuseppedarro.comanda.features.orders.domain.model.OrderStatus
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class OrderRepositoryImplTest {

    private lateinit var orderRepository: OrderRepositoryImpl
    private lateinit var orderApi: OrderApi
    private lateinit var menuApi: MenuApi

    @Before
    fun setUp() {
        orderApi = mockk()
        menuApi = mockk()
        orderRepository = OrderRepositoryImpl(orderApi, menuApi)
    }

    @Test
    fun getMenu_should_emit_success() = runTest {
        // Given
        val menuDto = listOf(MenuCategoryDto("Pizzas", listOf(MenuItemDto(id = "margherita", name = "Margherita", price = 1000))))
        coEvery { menuApi.getMenu() } returns menuDto

        // When
        val result = orderRepository.getMenu()

        // Then
        result.test {
            val success = awaitItem()
            assertThat(success.isSuccess).isTrue()
            assertThat(success.getOrNull()?.first()?.name).isEqualTo("Pizzas")
            awaitComplete()
        }
    }

    @Test
    fun getMenu_should_emit_error_when_api_throws_exception() = runTest {
        // Given
        coEvery { menuApi.getMenu() } throws RuntimeException("Error")

        // When
        val result = orderRepository.getMenu()

        // Then
        result.test {
            assertThat(awaitItem().isFailure).isTrue()
            awaitComplete()
        }
    }

    @Test
    fun getOrdersForTable_should_emit_success() = runTest {
        // Given
        val tableNumber = 1
        val orderResponse = GetOrderResponse(
            tableNumber = tableNumber,
            numberOfPeople = 4,
            status = "Open",
            items = listOf(OrderResponseItem(itemId = "margherita", quantity = 1, orderItemId = "1")),
            createdAt = "2024-05-16T10:00:00Z",
            subtotal = 1000,
            serviceCharge = 100,
            total = 1100
        )
        val menuDto = listOf(MenuCategoryDto("Pizzas", listOf(MenuItemDto(id = "margherita", name = "Margherita", price = 1000))))
        coEvery { orderApi.getOrdersForTable(tableNumber) } returns orderResponse
        coEvery { menuApi.getMenu() } returns menuDto

        // When
        val result = orderRepository.getOrdersForTable(tableNumber)

        // Then
        result.test {
            val success = awaitItem()
            assertThat(success.isSuccess).isTrue()
            val order = success.getOrNull()
            assertThat(order?.tableNumber).isEqualTo(tableNumber)
            assertThat(order?.status).isEqualTo(OrderStatus.OPEN)
            assertThat(order?.items?.first()?.menuItem?.name).isEqualTo("Margherita")
            awaitComplete()
        }
    }

    @Test
    fun getOrdersForTable_should_return_success_with_null_when_api_returns_null() = runTest {
        // Given
        val tableNumber = 1
        coEvery { orderApi.getOrdersForTable(tableNumber) } returns null

        // When
        val result = orderRepository.getOrdersForTable(tableNumber)

        // Then
        result.test {
            val success = awaitItem()
            assertThat(success.isSuccess).isTrue()
            assertThat(success.getOrNull()).isNull()
            awaitComplete()
        }
    }

    @Test
    fun getOrdersForTable_should_emit_error_when_api_throws_exception() = runTest {
        // Given
        val tableNumber = 1
        coEvery { orderApi.getOrdersForTable(tableNumber) } throws RuntimeException("Error")

        // When
        val result = orderRepository.getOrdersForTable(tableNumber)

        // Then
        result.test {
            assertThat(awaitItem().isFailure).isTrue()
            awaitComplete()
        }
    }

    @Test
    fun submitOrder_should_return_success_when_items_not_empty() = runTest {
        // Given
        val tableNumber = 1
        val numberOfPeople = 4
        val items = listOf(OrderItem(MenuItem("1", "Pizza", 1000), 1))
        coEvery { orderApi.submitOrder(any()) } returns Unit

        // When
        val result = orderRepository.submitOrder(tableNumber, numberOfPeople, items)

        // Then
        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun submitOrder_should_return_failure_when_items_empty() = runTest {
        // Given
        val tableNumber = 1
        val numberOfPeople = 4
        val items = emptyList<OrderItem>()

        // When
        val result = orderRepository.submitOrder(tableNumber, numberOfPeople, items)

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(OrderException.EmptyOrder)
    }

    @Test
    fun submitOrder_should_return_error_when_api_throws_exception() = runTest {
        // Given
        val tableNumber = 1
        val numberOfPeople = 4
        val items = listOf(OrderItem(MenuItem("1", "Pizza", 1000), 1))
        coEvery { orderApi.submitOrder(any()) } throws RuntimeException("Error")

        // When
        val result = orderRepository.submitOrder(tableNumber, numberOfPeople, items)

        // Then
        assertThat(result.isFailure).isTrue()
    }
}
