
package dev.giuseppedarro.comanda.features.orders.domain.use_case

import com.google.common.truth.Truth.assertThat
import dev.giuseppedarro.comanda.features.orders.domain.model.OrderItem
import dev.giuseppedarro.comanda.features.orders.domain.repository.OrderRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SubmitOrderUseCaseTest {

    private lateinit var submitOrderUseCase: SubmitOrderUseCase
    private lateinit var orderRepository: OrderRepository

    @Before
    fun setUp() {
        orderRepository = mockk()
        submitOrderUseCase = SubmitOrderUseCase(orderRepository)
    }

    @Test
    fun `invoke should return success when repository returns success`() = runTest {
        // Given
        val tableNumber = 1
        val numberOfPeople = 4
        val items = emptyList<OrderItem>()
        coEvery { orderRepository.submitOrder(tableNumber, numberOfPeople, items) } returns Result.success(Unit)

        // When
        val result = submitOrderUseCase(tableNumber, numberOfPeople, items)

        // Then
        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `invoke should return error when repository returns error`() = runTest {
        // Given
        val tableNumber = 1
        val numberOfPeople = 4
        val items = emptyList<OrderItem>()
        val errorMessage = "Error"
        coEvery { orderRepository.submitOrder(tableNumber, numberOfPeople, items) } returns Result.failure(java.lang.RuntimeException(errorMessage))

        // When
        val result = submitOrderUseCase(tableNumber, numberOfPeople, items)

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).isEqualTo(errorMessage)
    }
}
