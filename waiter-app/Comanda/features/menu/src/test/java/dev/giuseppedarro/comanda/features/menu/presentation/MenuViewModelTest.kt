package dev.giuseppedarro.comanda.features.menu.presentation

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.giuseppedarro.comanda.features.menu.domain.model.MenuCategory
import dev.giuseppedarro.comanda.features.menu.domain.usecase.GetMenuUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class MenuViewModelTest {

    private lateinit var viewModel: MenuViewModel
    private lateinit var getMenuUseCase: GetMenuUseCase
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getMenuUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `state is updated with menu when use case returns success`() = runTest {
        // Given
        val menu = listOf(MenuCategory("1", "Pizzas", emptyList()))
        coEvery { getMenuUseCase() } returns flowOf(Result.success(menu))

        // When
        viewModel = MenuViewModel(getMenuUseCase)

        // Then
        viewModel.uiState.test {
            val loadingState = awaitItem()
            assertThat(loadingState.isLoading).isTrue()

            val successState = awaitItem()
            assertThat(successState.isLoading).isFalse()
            assertThat(successState.categories).isEqualTo(menu)
            assertThat(successState.error).isNull()
        }
    }

    @Test
    fun `state is updated with error when use case returns failure`() = runTest {
        // Given
        val error = "Error"
        coEvery { getMenuUseCase() } returns flowOf(Result.failure(RuntimeException(error)))

        // When
        viewModel = MenuViewModel(getMenuUseCase)

        // Then
        viewModel.uiState.test {
            val loadingState = awaitItem()
            assertThat(loadingState.isLoading).isTrue()

            val errorState = awaitItem()
            assertThat(errorState.isLoading).isFalse()
            assertThat(errorState.categories).isEmpty()
            assertThat(errorState.error).isEqualTo(error)
        }
    }
}