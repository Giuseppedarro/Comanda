package dev.giuseppedarro.comanda.features.menu.domain.usecase

import com.google.common.truth.Truth.assertThat
import dev.giuseppedarro.comanda.features.menu.domain.model.MenuItem
import dev.giuseppedarro.comanda.features.menu.domain.repository.MenuRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpdateMenuItemUseCaseTest {

    private lateinit var updateMenuItemUseCase: UpdateMenuItemUseCase
    private lateinit var menuRepository: MenuRepository

    @Before
    fun setUp() {
        menuRepository = mockk()
        updateMenuItemUseCase = UpdateMenuItemUseCase(menuRepository)
    }

    @Test
    fun `invoke should call updateMenuItem on repository`() = runTest {
        // Given
        val menuItem = MenuItem("1", "Margherita", "Tomato and Mozzarella", 5.0)
        coEvery { menuRepository.updateMenuItem(menuItem) } returns Result.success(Unit)

        // When
        val result = updateMenuItemUseCase(menuItem)

        // Then
        assertThat(result.isSuccess).isTrue()
    }
}