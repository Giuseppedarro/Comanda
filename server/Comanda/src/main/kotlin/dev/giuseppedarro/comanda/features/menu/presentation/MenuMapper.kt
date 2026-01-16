package dev.giuseppedarro.comanda.features.menu.presentation

import dev.giuseppedarro.comanda.features.menu.domain.model.MenuCategory
import dev.giuseppedarro.comanda.features.menu.domain.model.MenuItem

fun MenuCategory.toDto(): MenuCategoryDto {
    return MenuCategoryDto(
        name = name,
        items = items.filter { it.isAvailable }.map { it.toDto() }
    )
}

fun MenuItem.toDto(): MenuItemDto {
    return MenuItemDto(
        id = id,
        name = name,
        price = "$${String.format("%.2f", price)}"
    )
}