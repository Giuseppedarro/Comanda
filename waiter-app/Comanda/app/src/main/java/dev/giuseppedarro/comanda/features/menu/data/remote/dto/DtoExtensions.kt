package dev.giuseppedarro.comanda.features.menu.data.remote.dto

import dev.giuseppedarro.comanda.features.menu.domain.model.MenuCategory
import dev.giuseppedarro.comanda.features.menu.domain.model.MenuItem

fun MenuCategoryDto.toDomain(): MenuCategory {
    return MenuCategory(
        id = id ?: "",
        name = name,
        displayOrder = displayOrder ?: 0,
        items = items.mapIndexed { index, item -> item.toDomain(id ?: "", index) }
    )
}

fun MenuCategory.toDto(): MenuCategoryDto {
    return MenuCategoryDto(
        name = name,
        items = items.map { it.toDto() },
        id = if (id.isNotBlank()) id else null,
        displayOrder = displayOrder
    )
}

fun MenuItemDto.toDomain(categoryId: String = "", displayOrder: Int = 0): MenuItem {
    // Parse price string: remove currency symbol and convert to Double
    val priceDouble = price.replace(Regex("[^\\d.,]"), "").replace(",", ".").toDoubleOrNull() ?: 0.0

    return MenuItem(
        id = id,
        categoryId = this.categoryId ?: categoryId,
        name = name,
        description = description ?: "",
        price = priceDouble,
        isAvailable = isAvailable ?: true,
        displayOrder = this.displayOrder ?: displayOrder
    )
}

fun MenuItem.toDto(): MenuItemDto {
    return MenuItemDto(
        id = id,
        name = name,
        price = String.format("$%.2f", price),
        categoryId = if (categoryId.isNotBlank()) categoryId else null,
        description = if (description.isNotBlank()) description else null,
        isAvailable = isAvailable,
        displayOrder = displayOrder
    )
}
