package dev.giuseppedarro.comanda.features.menu.domain.usecase

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.giuseppedarro.comanda.features.menu.domain.model.MenuCategory
import dev.giuseppedarro.comanda.features.menu.domain.repository.MenuRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetMenuUseCaseTest {

    private lateinit var getMenuUseCase: GetMenuUseCase
    private lateinit var menuRepository: MenuRepository

    @Before
    fun setUp() {
        menuRepository = mockk()
        getMenuUseCase = GetMenuUseCase(menuRepository)
    }

    @Test
    fun `invoke should return menu from repository`() = runTest {
        // Given
        val menu = listOf(MenuCategory("1", "Pizzas", emptyList()))
        coEvery { menuRepository.getMenu() } returns flowOf(Result.success(menu))

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
}