package dev.giuseppedarro.comanda.features.orders.use_case

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.giuseppedarro.comanda.features.orders.domain.model.MenuCategory
import dev.giuseppedarro.comanda.features.orders.domain.repository.OrderRepository
import dev.giuseppedarro.comanda.features.orders.domain.use_case.GetMenuUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetMenuUseCaseTest {

    private lateinit var getMenuUseCase: GetMenuUseCase
    private lateinit var orderRepository: OrderRepository

    @Before
    fun setUp() {
        orderRepository = mockk()
        getMenuUseCase = GetMenuUseCase(orderRepository)
    }

    @Test
    fun `invoke should return menu when repository returns success`() = runTest {
        // Given
        val menu = listOf(MenuCategory("Pizzas", emptyList()))
        coEvery { orderRepository.getMenu() } returns flowOf(Result.success(menu))

        // When
        val result = getMenuUseCase()

        // Then
        result.test {
            val emission = awaitItem()
            assertThat(emission.isSuccess).isTrue()
            assertThat(emission.getOrNull()).isEqualTo(menu)
            awaitComplete()
        }
    }

    @Test
    fun `invoke should return error when repository returns error`() = runTest {
        // Given
        val errorMessage = "Error"
        coEvery { orderRepository.getMenu() } returns flowOf(Result.failure(RuntimeException(errorMessage)))

        // When
        val result = getMenuUseCase()

        // Then
        result.test {
            val emission = awaitItem()
            assertThat(emission.isFailure).isTrue()
            assertThat(emission.exceptionOrNull()?.message).isEqualTo(errorMessage)
            awaitComplete()
        }
    }
}
