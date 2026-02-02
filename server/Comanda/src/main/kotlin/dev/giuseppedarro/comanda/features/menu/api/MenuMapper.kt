package dev.giuseppedarro.comanda.features.menu.api

import dev.giuseppedarro.comanda.features.menu.domain.model.MenuCategory
import dev.giuseppedarro.comanda.features.menu.domain.model.MenuItem

fun MenuCategory.toDto(): MenuCategoryDto {
    return MenuCategoryDto(
        id = id,
        name = name,
        items = items.filter { it.isAvailable }.map { it.toDto() }
    )
}

fun MenuItem.toDto(): MenuItemDto {
    return MenuItemDto(
        id = id,
        categoryId = categoryId,
        name = name,
        price = price
    )
}