package dev.giuseppedarro.comanda.features.menu.domain.usecase

import com.google.common.truth.Truth.assertThat
import dev.giuseppedarro.comanda.features.menu.domain.repository.MenuRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DeleteMenuItemUseCaseTest {

    private lateinit var deleteMenuItemUseCase: DeleteMenuItemUseCase
    private lateinit var menuRepository: MenuRepository

    @Before
    fun setUp() {
        menuRepository = mockk()
        deleteMenuItemUseCase = DeleteMenuItemUseCase(menuRepository)
    }

    @Test
    fun `invoke should call deleteMenuItem on repository`() = runTest {
        // Given
        val menuItemId = "1"
        coEvery { menuRepository.deleteMenuItem(menuItemId) } returns Result.success(Unit)

        // When
        val result = deleteMenuItemUseCase(menuItemId)

        // Then
        assertThat(result.isSuccess).isTrue()
    }
}