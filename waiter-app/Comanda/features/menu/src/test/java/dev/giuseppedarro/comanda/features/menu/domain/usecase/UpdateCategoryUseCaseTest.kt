package dev.giuseppedarro.comanda.features.menu.domain.usecase

import com.google.common.truth.Truth.assertThat
import dev.giuseppedarro.comanda.features.menu.domain.model.MenuCategory
import dev.giuseppedarro.comanda.features.menu.domain.repository.MenuRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpdateCategoryUseCaseTest {

    private lateinit var updateCategoryUseCase: UpdateCategoryUseCase
    private lateinit var menuRepository: MenuRepository

    @Before
    fun setUp() {
        menuRepository = mockk()
        updateCategoryUseCase = UpdateCategoryUseCase(menuRepository)
    }

    @Test
    fun `invoke should call updateCategory on repository`() = runTest {
        // Given
        val category = MenuCategory("1", "Pizzas", 0, emptyList())
        coEvery { menuRepository.updateCategory(category) } returns Result.success(Unit)

        // When
        val result = updateCategoryUseCase(category)

        // Then
        assertThat(result.isSuccess).isTrue()
    }
}