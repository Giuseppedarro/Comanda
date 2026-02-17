package dev.giuseppedarro.comanda.features.menu.data.repository

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.giuseppedarro.comanda.features.menu.data.remote.MenuApi
import dev.giuseppedarro.comanda.features.menu.data.remote.dto.MenuCategoryDto
import dev.giuseppedarro.comanda.features.menu.domain.model.MenuCategory
import dev.giuseppedarro.comanda.features.menu.domain.model.MenuItem
import dev.giuseppedarro.comanda.features.menu.domain.repository.MenuRepository
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class MenuRepositoryImplTest {

    private lateinit var menuRepository: MenuRepository
    private lateinit var menuApi: MenuApi

    @Before
    fun setUp() {
        menuApi = mockk()
        menuRepository = MenuRepositoryImpl(menuApi)
    }

    @Test
    fun `getMenu should return success when api is successful`() = runTest {
        // Given
        val menuCategoryDtos = listOf(MenuCategoryDto("1", "Pizzas", emptyList()))
        coEvery { menuApi.getMenu() } returns menuCategoryDtos

        // When
        val result = menuRepository.getMenu()

        // Then
        result.test {
            val emission = awaitItem()
            assertThat(emission.isSuccess).isTrue()
            val categories = emission.getOrNull()
            assertThat(categories).isNotNull()
            assertThat(categories!!).hasSize(1)
            assertThat(categories[0].id).isEqualTo("1")
            assertThat(categories[0].name).isEqualTo("Pizzas")
            awaitComplete()
        }
    }

    @Test
    fun `getMenu should return failure when api throws exception`() = runTest {
        // Given
        val exception = RuntimeException("Error")
        coEvery { menuApi.getMenu() } throws exception

        // When
        val result = menuRepository.getMenu()

        // Then
        result.test {
            val emission = awaitItem()
            assertThat(emission.isFailure).isTrue()
            assertThat(emission.exceptionOrNull()).isEqualTo(exception)
            awaitComplete()
        }
    }

    @Test
    fun `createCategory should return success when api is successful`() = runTest {
        // Given
        val category = MenuCategory("1", "Pizzas", emptyList())
        coJustRun { menuApi.createCategory(any()) }

        // When
        val result = menuRepository.createCategory(category)

        // Then
        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `createCategory should return failure when api throws exception`() = runTest {
        // Given
        val category = MenuCategory("1", "Pizzas", emptyList())
        val exception = RuntimeException("Error")
        coEvery { menuApi.createCategory(any()) } throws exception

        // When
        val result = menuRepository.createCategory(category)

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
    }

    @Test
    fun `updateCategory should return success when api is successful`() = runTest {
        // Given
        val category = MenuCategory("1", "Pizzas", emptyList())
        coJustRun { menuApi.updateCategory(any(), any()) }

        // When
        val result = menuRepository.updateCategory(category)

        // Then
        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `updateCategory should return failure when api throws exception`() = runTest {
        // Given
        val category = MenuCategory("1", "Pizzas", emptyList())
        val exception = RuntimeException("Error")
        coEvery { menuApi.updateCategory(any(), any()) } throws exception

        // When
        val result = menuRepository.updateCategory(category)

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
    }

    @Test
    fun `deleteCategory should return success when api is successful`() = runTest {
        // Given
        val categoryId = "1"
        coJustRun { menuApi.deleteCategory(any()) }

        // When
        val result = menuRepository.deleteCategory(categoryId)

        // Then
        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `deleteCategory should return failure when api throws exception`() = runTest {
        // Given
        val categoryId = "1"
        val exception = RuntimeException("Error")
        coEvery { menuApi.deleteCategory(any()) } throws exception

        // When
        val result = menuRepository.deleteCategory(categoryId)

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
    }

    @Test
    fun `addMenuItem should return success when api is successful`() = runTest {
        // Given
        val categoryId = "1"
        val menuItem = MenuItem("1", "Margherita", "Tomato and Mozzarella", 5.0)
        coJustRun { menuApi.addMenuItem(any(), any()) }

        // When
        val result = menuRepository.addMenuItem(categoryId, menuItem)

        // Then
        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `addMenuItem should return failure when api throws exception`() = runTest {
        // Given
        val categoryId = "1"
        val menuItem = MenuItem("1", "Margherita", "Tomato and Mozzarella", 5.0)
        val exception = RuntimeException("Error")
        coEvery { menuApi.addMenuItem(any(), any()) } throws exception

        // When
        val result = menuRepository.addMenuItem(categoryId, menuItem)

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
    }

    @Test
    fun `updateMenuItem should return success when api is successful`() = runTest {
        // Given
        val menuItem = MenuItem("1", "Margherita", "Tomato and Mozzarella", 5.0)
        coJustRun { menuApi.updateMenuItem(any(), any()) }

        // When
        val result = menuRepository.updateMenuItem(menuItem)

        // Then
        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `updateMenuItem should return failure when api throws exception`() = runTest {
        // Given
        val menuItem = MenuItem("1", "Margherita", "Tomato and Mozzarella", 5.0)
        val exception = RuntimeException("Error")
        coEvery { menuApi.updateMenuItem(any(), any()) } throws exception

        // When
        val result = menuRepository.updateMenuItem(menuItem)

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
    }

    @Test
    fun `deleteMenuItem should return success when api is successful`() = runTest {
        // Given
        val menuItemId = "1"
        coJustRun { menuApi.deleteMenuItem(any()) }

        // When
        val result = menuRepository.deleteMenuItem(menuItemId)

        // Then
        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `deleteMenuItem should return failure when api throws exception`() = runTest {
        // Given
        val menuItemId = "1"
        val exception = RuntimeException("Error")
        coEvery { menuApi.deleteMenuItem(any()) } throws exception

        // When
        val result = menuRepository.deleteMenuItem(menuItemId)

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
    }
}