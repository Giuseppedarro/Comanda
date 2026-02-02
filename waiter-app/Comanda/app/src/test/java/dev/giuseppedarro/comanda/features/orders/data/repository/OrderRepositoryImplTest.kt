
package dev.giuseppedarro.comanda.features.orders.data.repository

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.giuseppedarro.comanda.core.utils.Result
import dev.giuseppedarro.comanda.features.orders.data.remote.OrderApi
import dev.giuseppedarro.comanda.features.orders.data.remote.dto.GetOrderResponse
import dev.giuseppedarro.comanda.features.orders.data.remote.dto.MenuCategoryDto
import dev.giuseppedarro.comanda.features.orders.data.remote.dto.MenuItemDto
import dev.giuseppedarro.comanda.features.orders.data.remote.dto.OrderResponseItem
import dev.giuseppedarro.comanda.features.orders.domain.model.OrderStatus
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class OrderRepositoryImplTest {

    private lateinit var orderRepository: OrderRepositoryImpl
    private lateinit var orderApi: OrderApi

    @Before
    fun setUp() {
        orderApi = mockk()
        orderRepository = OrderRepositoryImpl(orderApi)
    }

    @Test
    fun getMenu_should_emit_loading_and_then_success() = runTest {
        // Given
        val menuDto = listOf(MenuCategoryDto("Pizzas", listOf(MenuItemDto(id = "margherita", name = "Margherita", price = 1000))))
        coEvery { orderApi.getMenu() } returns menuDto

        // When
        val result = orderRepository.getMenu()

        // Then
        result.test {
            assertThat(awaitItem()).isInstanceOf(Result.Loading::class.java)
            val success = awaitItem()
            assertThat(success).isInstanceOf(Result.Success::class.java)
            assertThat((success as Result.Success).data?.first()?.name).isEqualTo("Pizzas")
            awaitComplete()
        }
    }

    @Test
    fun getMenu_should_emit_error_when_api_throws_exception() = runTest {
        // Given
        coEvery { orderApi.getMenu() } throws RuntimeException("Error")

        // When
        val result = orderRepository.getMenu()

        // Then
        result.test {
            assertThat(awaitItem()).isInstanceOf(Result.Loading::class.java)
            assertThat(awaitItem()).isInstanceOf(Result.Error::class.java)
            awaitComplete()
        }
    }

    @Test
    fun getOrdersForTable_should_emit_loading_and_then_success() = runTest {
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
        coEvery { orderApi.getMenu() } returns menuDto

        // When
        val result = orderRepository.getOrdersForTable(tableNumber)

        // Then
        result.test {
            assertThat(awaitItem()).isInstanceOf(Result.Loading::class.java)
            val success = awaitItem()
            assertThat(success).isInstanceOf(Result.Success::class.java)
            val order = (success as Result.Success).data
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
            assertThat(awaitItem()).isInstanceOf(Result.Loading::class.java)
            val success = awaitItem()
            assertThat(success).isInstanceOf(Result.Success::class.java)
            assertThat((success as Result.Success).data).isNull()
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
            assertThat(awaitItem()).isInstanceOf(Result.Loading::class.java)
            assertThat(awaitItem()).isInstanceOf(Result.Error::class.java)
            awaitComplete()
        }
    }

    @Test
    fun submitOrder_should_return_success() = runTest {
        // Given
        val tableNumber = 1
        val numberOfPeople = 4
        coEvery { orderApi.submitOrder(any()) } returns Unit

        // When
        val result = orderRepository.submitOrder(tableNumber, numberOfPeople, emptyList())

        // Then
        assertThat(result).isInstanceOf(Result.Success::class.java)
    }

    @Test
    fun submitOrder_should_return_error_when_api_throws_exception() = runTest {
        // Given
        val tableNumber = 1
        val numberOfPeople = 4
        coEvery { orderApi.submitOrder(any()) } throws RuntimeException("Error")

        // When
        val result = orderRepository.submitOrder(tableNumber, numberOfPeople, emptyList())

        // Then
        assertThat(result).isInstanceOf(Result.Error::class.java)
    }
}
