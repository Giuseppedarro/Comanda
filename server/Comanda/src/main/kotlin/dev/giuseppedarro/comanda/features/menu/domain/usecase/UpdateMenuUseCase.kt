package dev.giuseppedarro.comanda.features.menu.domain.usecase

import dev.giuseppedarro.comanda.features.menu.domain.model.MenuCategory
import dev.giuseppedarro.comanda.features.menu.domain.model.MenuItem
import dev.giuseppedarro.comanda.features.menu.domain.repository.MenuRepository
import dev.giuseppedarro.comanda.features.menu.presentation.MenuItemUpdateDto
import dev.giuseppedarro.comanda.features.menu.presentation.MenuCategoryUpdateDto

class UpdateMenuUseCase(
    private val menuRepository: MenuRepository
) {
    suspend operator fun invoke(categories: List<MenuCategoryUpdateDto>): Result<Unit> {
        // Validation
        val validationError = validateMenu(categories)
        if (validationError != null) {
            return Result.failure(IllegalArgumentException(validationError))
        }

        // Convert DTOs to domain models
        val domainCategories = categories.map { categoryDto ->
            val items = categoryDto.items.map { itemDto ->
                MenuItem(
                    id = itemDto.id,
                    categoryId = itemDto.categoryId,
                    name = itemDto.name.trim(),
                    price = itemDto.price,
                    description = itemDto.description?.trim(),
                    isAvailable = itemDto.isAvailable
                )
            }

            MenuCategory(
                id = categoryDto.id,
                name = categoryDto.name.trim(),
                items = items
            )
        }

        return menuRepository.updateMenu(domainCategories)
    }

    private fun validateMenu(categories: List<MenuCategoryUpdateDto>): String? {
        // Validate categories are not empty
        if (categories.isEmpty()) {
            return "Menu must contain at least one category"
        }

        // Check for duplicate category IDs
        val categoryIds = categories.map { it.id }
        if (categoryIds.size != categoryIds.toSet().size) {
            return "Duplicate category IDs found"
        }

        // Check for duplicate category names
        val categoryNames = categories.map { it.name.lowercase() }
        if (categoryNames.size != categoryNames.toSet().size) {
            return "Duplicate category names found"
        }

        // Validate each category
        for (category in categories) {
            // Validate category name
            if (category.name.isBlank()) {
                return "Category name cannot be empty"
            }

            // Validate category ID
            if (category.id.isBlank()) {
                return "Category ID cannot be empty"
            }

            // Validate items in this category
            for (item in category.items) {
                // Validate item references correct category
                if (item.categoryId != category.id) {
                    return "Item '${item.name}' has categoryId '${item.categoryId}' but belongs to category '${category.id}'"
                }

                // Validate item ID
                if (item.id.isBlank()) {
                    return "Item ID cannot be empty for item in category '${category.name}'"
                }

                // Validate item name
                if (item.name.isBlank()) {
                    return "Item name cannot be empty in category '${category.name}'"
                }

                // Validate price is positive
                if (item.price < 0) {
                    return "Price cannot be negative for item '${item.name}' in category '${category.name}'"
                }

                // Check for duplicate item IDs within the same category
                val itemIds = category.items.map { it.id }
                if (itemIds.size != itemIds.toSet().size) {
                    return "Duplicate item IDs found in category '${category.name}'"
                }
            }
        }

        return null
    }
}