package dev.giuseppedarro.comanda.features.menu.data.remote.dto

import dev.giuseppedarro.comanda.features.menu.domain.model.MenuCategory
import dev.giuseppedarro.comanda.features.menu.domain.model.MenuItem

fun MenuCategoryDto.toDomain(): MenuCategory {
    return MenuCategory(
        id = id ?: "",
        name = name,
        displayOrder = displayOrder,
        items = items.map { it.toDomain() }
    )
}

fun MenuCategory.toDto(): MenuCategoryDto {
    return MenuCategoryDto(
        id = if (id.isNotBlank()) id else null,
        name = name,
        displayOrder = displayOrder,
        items = items.map { it.toDto() }
    )
}

fun MenuItemDto.toDomain(): MenuItem {
    return MenuItem(
        id = id ?: "",
        categoryId = categoryId ?: "",
        name = name,
        description = description,
        price = price,
        isAvailable = isAvailable,
        displayOrder = displayOrder
    )
}

fun MenuItem.toDto(): MenuItemDto {
    return MenuItemDto(
        id = if (id.isNotBlank()) id else null,
        categoryId = if (categoryId.isNotBlank()) categoryId else null,
        name = name,
        description = description,
        price = price,
        isAvailable = isAvailable,
        displayOrder = displayOrder
    )
}
