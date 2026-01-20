package dev.giuseppedarro.comanda.features.menu.domain.usecase

import dev.giuseppedarro.comanda.features.menu.domain.repository.MenuRepository
import dev.giuseppedarro.comanda.features.menu.presentation.MenuItemUpdateDto
import dev.giuseppedarro.comanda.features.menu.presentation.MenuCategoryUpdateDto
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class UpdateMenuUseCaseTest {

    @Test
    fun `should successfully update menu with valid data`() = runTest {
        // Given
        val categories = listOf(
            MenuCategoryUpdateDto(
                id = "cat-1",
                name = "Appetizers",
                displayOrder = 1,
                items = listOf(
                    MenuItemUpdateDto(
                        id = "item-1",
                        categoryId = "cat-1",
                        name = "Bruschetta",
                        price = 899,
                        description = "Toasted bread",
                        isAvailable = true,
                        displayOrder = 1
                    )
                )
            )
        )

        val mockRepository = mockk<MenuRepository>()
        coEvery { mockRepository.updateMenu(any()) } returns Result.success(Unit)

        val useCase = UpdateMenuUseCase(mockRepository)

        // When
        val result = useCase(categories)

        // Then
        assertTrue(result.isSuccess)
    }

    @Test
    fun `should fail validation when menu is empty`() = runTest {
        // Given
        val emptyCategories = emptyList<MenuCategoryUpdateDto>()
        val mockRepository = mockk<MenuRepository>()
        val useCase = UpdateMenuUseCase(mockRepository)

        // When
        val result = useCase(emptyCategories)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()!!.message!!.contains("at least one category"))
    }

    @Test
    fun `should fail validation when category ID is blank`() = runTest {
        // Given
        val categories = listOf(
            MenuCategoryUpdateDto(
                id = "   ",
                name = "Appetizers",
                items = emptyList()
            )
        )
        val mockRepository = mockk<MenuRepository>()
        val useCase = UpdateMenuUseCase(mockRepository)

        // When
        val result = useCase(categories)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()!!.message!!.contains("Category ID cannot be empty"))
    }

    @Test
    fun `should fail validation when category name is blank`() = runTest {
        // Given
        val categories = listOf(
            MenuCategoryUpdateDto(
                id = "cat-1",
                name = "",
                items = emptyList()
            )
        )
        val mockRepository = mockk<MenuRepository>()
        val useCase = UpdateMenuUseCase(mockRepository)

        // When
        val result = useCase(categories)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()!!.message!!.contains("Category name cannot be empty"))
    }

    @Test
    fun `should fail validation when duplicate category IDs exist`() = runTest {
        // Given
        val categories = listOf(
            MenuCategoryUpdateDto(
                id = "cat-1",
                name = "Appetizers",
                items = emptyList()
            ),
            MenuCategoryUpdateDto(
                id = "cat-1", // Duplicate ID
                name = "Mains",
                items = emptyList()
            )
        )
        val mockRepository = mockk<MenuRepository>()
        val useCase = UpdateMenuUseCase(mockRepository)

        // When
        val result = useCase(categories)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()!!.message!!.contains("Duplicate category IDs"))
    }

    @Test
    fun `should fail validation when duplicate category names exist`() = runTest {
        // Given
        val categories = listOf(
            MenuCategoryUpdateDto(
                id = "cat-1",
                name = "Appetizers",
                items = emptyList()
            ),
            MenuCategoryUpdateDto(
                id = "cat-2",
                name = "Appetizers", // Duplicate name
                items = emptyList()
            )
        )
        val mockRepository = mockk<MenuRepository>()
        val useCase = UpdateMenuUseCase(mockRepository)

        // When
        val result = useCase(categories)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()!!.message!!.contains("Duplicate category names"))
    }

    @Test
    fun `should fail validation when item has wrong category ID`() = runTest {
        // Given
        val categories = listOf(
            MenuCategoryUpdateDto(
                id = "cat-1",
                name = "Appetizers",
                items = listOf(
                    MenuItemUpdateDto(
                        id = "item-1",
                        categoryId = "cat-999", // Wrong category ID
                        name = "Bruschetta",
                        price = 899
                    )
                )
            )
        )
        val mockRepository = mockk<MenuRepository>()
        val useCase = UpdateMenuUseCase(mockRepository)

        // When
        val result = useCase(categories)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()!!.message!!.contains("categoryId"))
    }

    @Test
    fun `should fail validation when item ID is blank`() = runTest {
        // Given
        val categories = listOf(
            MenuCategoryUpdateDto(
                id = "cat-1",
                name = "Appetizers",
                items = listOf(
                    MenuItemUpdateDto(
                        id = "",
                        categoryId = "cat-1",
                        name = "Bruschetta",
                        price = 899
                    )
                )
            )
        )
        val mockRepository = mockk<MenuRepository>()
        val useCase = UpdateMenuUseCase(mockRepository)

        // When
        val result = useCase(categories)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()!!.message!!.contains("Item ID cannot be empty"))
    }

    @Test
    fun `should fail validation when item name is blank`() = runTest {
        // Given
        val categories = listOf(
            MenuCategoryUpdateDto(
                id = "cat-1",
                name = "Appetizers",
                items = listOf(
                    MenuItemUpdateDto(
                        id = "item-1",
                        categoryId = "cat-1",
                        name = "  ",
                        price = 899
                    )
                )
            )
        )
        val mockRepository = mockk<MenuRepository>()
        val useCase = UpdateMenuUseCase(mockRepository)

        // When
        val result = useCase(categories)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()!!.message!!.contains("Item name cannot be empty"))
    }

    @Test
    fun `should fail validation when item price is negative`() = runTest {
        // Given
        val categories = listOf(
            MenuCategoryUpdateDto(
                id = "cat-1",
                name = "Appetizers",
                items = listOf(
                    MenuItemUpdateDto(
                        id = "item-1",
                        categoryId = "cat-1",
                        name = "Bruschetta",
                        price = -599 // Negative price
                    )
                )
            )
        )
        val mockRepository = mockk<MenuRepository>()
        val useCase = UpdateMenuUseCase(mockRepository)

        // When
        val result = useCase(categories)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()!!.message!!.contains("Price cannot be negative"))
    }

    @Test
    fun `should fail validation when duplicate item IDs in same category`() = runTest {
        // Given
        val categories = listOf(
            MenuCategoryUpdateDto(
                id = "cat-1",
                name = "Appetizers",
                items = listOf(
                    MenuItemUpdateDto(
                        id = "item-1",
                        categoryId = "cat-1",
                        name = "Bruschetta",
                        price = 899
                    ),
                    MenuItemUpdateDto(
                        id = "item-1", // Duplicate ID
                        categoryId = "cat-1",
                        name = "Garlic Bread",
                        price = 699
                    )
                )
            )
        )
        val mockRepository = mockk<MenuRepository>()
        val useCase = UpdateMenuUseCase(mockRepository)

        // When
        val result = useCase(categories)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()!!.message!!.contains("Duplicate item IDs"))
    }

    @Test
    fun `should trim category and item names`() = runTest {
        // Given
        val categories = listOf(
            MenuCategoryUpdateDto(
                id = "cat-1",
                name = "  Appetizers  ",
                items = listOf(
                    MenuItemUpdateDto(
                        id = "item-1",
                        categoryId = "cat-1",
                        name = "  Bruschetta  ",
                        price = 899,
                        description = "  Toasted bread  "
                    )
                )
            )
        )

        val mockRepository = mockk<MenuRepository>()
        val capturedMenu = mutableListOf<List<dev.giuseppedarro.comanda.features.menu.domain.model.MenuCategory>>()
        coEvery { mockRepository.updateMenu(capture(capturedMenu)) } returns Result.success(Unit)
        val useCase = UpdateMenuUseCase(mockRepository)

        // When
        val result = useCase(categories)

        // Then
        assertTrue(result.isSuccess)
        // Verify the repository was called with trimmed values
        assertEquals("Appetizers", capturedMenu[0][0].name)
        assertEquals("Bruschetta", capturedMenu[0][0].items[0].name)
        assertEquals("Toasted bread", capturedMenu[0][0].items[0].description)
    }

    @Test
    fun `should handle multiple categories and items`() = runTest {
        // Given
        val categories = listOf(
            MenuCategoryUpdateDto(
                id = "cat-1",
                name = "Appetizers",
                items = listOf(
                    MenuItemUpdateDto(
                        id = "item-1",
                        categoryId = "cat-1",
                        name = "Bruschetta",
                        price = 899
                    ),
                    MenuItemUpdateDto(
                        id = "item-2",
                        categoryId = "cat-1",
                        name = "Garlic Bread",
                        price = 699
                    )
                )
            ),
            MenuCategoryUpdateDto(
                id = "cat-2",
                name = "Mains",
                items = listOf(
                    MenuItemUpdateDto(
                        id = "item-3",
                        categoryId = "cat-2",
                        name = "Carbonara",
                        price = 1499
                    )
                )
            )
        )

        val mockRepository = mockk<MenuRepository>()
        coEvery { mockRepository.updateMenu(any()) } returns Result.success(Unit)
        val useCase = UpdateMenuUseCase(mockRepository)

        // When
        val result = useCase(categories)

        // Then
        assertTrue(result.isSuccess)
    }

    @Test
    fun `should fail validation when category names differ only by case`() = runTest {
        // Given
        val categories = listOf(
            MenuCategoryUpdateDto(
                id = "cat-1",
                name = "Appetizers",
                items = emptyList()
            ),
            MenuCategoryUpdateDto(
                id = "cat-2",
                name = "APPETIZERS", // Same name, different case
                items = emptyList()
            )
        )
        val mockRepository = mockk<MenuRepository>()
        val useCase = UpdateMenuUseCase(mockRepository)

        // When
        val result = useCase(categories)

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()!!.message!!.contains("Duplicate category names"))
    }

    @Test
    fun `should propagate repository failures`() = runTest {
        // Given
        val categories = listOf(
            MenuCategoryUpdateDto(
                id = "cat-1",
                name = "Appetizers",
                items = listOf(
                    MenuItemUpdateDto(
                        id = "item-1",
                        categoryId = "cat-1",
                        name = "Bruschetta",
                        price = 899
                    )
                )
            )
        )

        val mockRepository = mockk<MenuRepository>()
        coEvery { mockRepository.updateMenu(any()) } returns Result.failure(Exception("Database error"))
        val useCase = UpdateMenuUseCase(mockRepository)

        // When
        val result = useCase(categories)

        // Then
        assertTrue(result.isFailure)
        assertEquals("Database error", result.exceptionOrNull()?.message)
    }
}