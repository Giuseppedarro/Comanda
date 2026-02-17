package dev.giuseppedarro.comanda.features.menu.domain.usecase

import com.google.common.truth.Truth.assertThat
import dev.giuseppedarro.comanda.features.menu.domain.repository.MenuRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DeleteCategoryUseCaseTest {

    private lateinit var deleteCategoryUseCase: DeleteCategoryUseCase
    private lateinit var menuRepository: MenuRepository

    @Before
    fun setUp() {
        menuRepository = mockk()
        deleteCategoryUseCase = DeleteCategoryUseCase(menuRepository)
    }

    @Test
    fun `invoke should call deleteCategory on repository`() = runTest {
        // Given
        val categoryId = "1"
        coEvery { menuRepository.deleteCategory(categoryId) } returns Result.success(Unit)

        // When
        val result = deleteCategoryUseCase(categoryId)

        // Then
        assertThat(result.isSuccess).isTrue()
    }
}