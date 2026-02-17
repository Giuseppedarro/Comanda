package dev.giuseppedarro.comanda.features.menu.domain.usecase

import com.google.common.truth.Truth.assertThat
import dev.giuseppedarro.comanda.features.menu.domain.model.MenuItem
import dev.giuseppedarro.comanda.features.menu.domain.repository.MenuRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AddMenuItemUseCaseTest {

    private lateinit var addMenuItemUseCase: AddMenuItemUseCase
    private lateinit var menuRepository: MenuRepository

    @Before
    fun setUp() {
        menuRepository = mockk()
        addMenuItemUseCase = AddMenuItemUseCase(menuRepository)
    }

    @Test
    fun `invoke should call addMenuItem on repository`() = runTest {
        // Given
        val categoryId = "1"
        val menuItem = MenuItem("1", "Margherita", "Tomato and Mozzarella", 5.0)
        coEvery { menuRepository.addMenuItem(categoryId, menuItem) } returns Result.success(Unit)

        // When
        val result = addMenuItemUseCase(categoryId, menuItem)

        // Then
        assertThat(result.isSuccess).isTrue()
    }
}