package dev.giuseppedarro.comanda.features.menu.domain.usecase

import com.google.common.truth.Truth.assertThat
import dev.giuseppedarro.comanda.features.menu.domain.model.MenuCategory
import dev.giuseppedarro.comanda.features.menu.domain.repository.MenuRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class CreateCategoryUseCaseTest {

    private lateinit var createCategoryUseCase: CreateCategoryUseCase
    private lateinit var menuRepository: MenuRepository

    @Before
    fun setUp() {
        menuRepository = mockk()
        createCategoryUseCase = CreateCategoryUseCase(menuRepository)
    }

    @Test
    fun `invoke should call createCategory on repository`() = runTest {
        // Given
        val category = MenuCategory("1", "Pizzas", 0, emptyList())
        coEvery { menuRepository.createCategory(category) } returns Result.success(Unit)

        // When
        val result = createCategoryUseCase(category)

        // Then
        assertThat(result.isSuccess).isTrue()
    }
}