package dev.giuseppedarro.comanda.features.orders.usecase

import com.google.common.truth.Truth.assertThat
import dev.giuseppedarro.comanda.core.domain.model.DomainException
import dev.giuseppedarro.comanda.features.orders.domain.model.OrderItem
import dev.giuseppedarro.comanda.features.orders.domain.repository.OrderRepository
import dev.giuseppedarro.comanda.features.orders.domain.usecase.SubmitOrderUseCase
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
        val exception = DomainException.UnknownException(errorMessage)
        coEvery { orderRepository.submitOrder(tableNumber, numberOfPeople, items) } returns Result.failure(exception)

        // When
        val result = submitOrderUseCase(tableNumber, numberOfPeople, items)

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
    }
}
