package dev.giuseppedarro.comanda.features.orders.use_case

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.giuseppedarro.comanda.features.orders.domain.model.Order
import dev.giuseppedarro.comanda.features.orders.domain.model.OrderItem
import dev.giuseppedarro.comanda.features.orders.domain.model.OrderStatus
import dev.giuseppedarro.comanda.features.orders.domain.repository.OrderRepository
import dev.giuseppedarro.comanda.features.orders.domain.use_case.GetOrdersForTableUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetOrdersForTableUseCaseTest {

    private lateinit var getOrdersForTableUseCase: GetOrdersForTableUseCase
    private lateinit var orderRepository: OrderRepository

    @Before
    fun setUp() {
        orderRepository = mockk()
        getOrdersForTableUseCase = GetOrdersForTableUseCase(orderRepository)
    }

    @Test
    fun `invoke should return order when repository returns success`() = runTest {
        // Given
        val tableNumber = 1
        val order = Order(
            tableNumber = tableNumber,
            numberOfPeople = 4,
            status = OrderStatus.OPEN,
            items = emptyList<OrderItem>(),
            createdAt = "2026-01-16T10:00:00Z"
        )
        coEvery { orderRepository.getOrdersForTable(tableNumber) } returns flowOf(Result.success(order))

        // When
        val result = getOrdersForTableUseCase(tableNumber)

        // Then
        result.test {
            val emission = awaitItem()
            assertThat(emission.isSuccess).isTrue()
            assertThat(emission.getOrNull()).isEqualTo(order)
            awaitComplete()
        }
    }

    @Test
    fun `invoke should return null when repository returns success with null`() = runTest {
        // Given
        val tableNumber = 1
        coEvery { orderRepository.getOrdersForTable(tableNumber) } returns flowOf(Result.success(null))

        // When
        val result = getOrdersForTableUseCase(tableNumber)

        // Then
        result.test {
            val emission = awaitItem()
            assertThat(emission.isSuccess).isTrue()
            assertThat(emission.getOrNull()).isNull()
            awaitComplete()
        }
    }

    @Test
    fun `invoke should return error when repository returns error`() = runTest {
        // Given
        val tableNumber = 1
        val errorMessage = "Error"
        coEvery { orderRepository.getOrdersForTable(tableNumber) } returns flowOf(Result.failure(RuntimeException(errorMessage)))

        // When
        val result = getOrdersForTableUseCase(tableNumber)

        // Then
        result.test {
            val emission = awaitItem()
            assertThat(emission.isFailure).isTrue()
            assertThat(emission.exceptionOrNull()?.message).isEqualTo(errorMessage)
            awaitComplete()
        }
    }
}
