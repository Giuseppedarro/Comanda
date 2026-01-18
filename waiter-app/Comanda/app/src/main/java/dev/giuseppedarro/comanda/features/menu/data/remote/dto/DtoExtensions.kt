package dev.giuseppedarro.comanda.features.menu.data.remote.dto

import dev.giuseppedarro.comanda.features.menu.domain.model.MenuItem as DomainMenuItem
import dev.giuseppedarro.comanda.features.menu.domain.model.MenuCategory as DomainMenuCategory

fun MenuCategoryDto.toDomain(): DomainMenuCategory {
    return DomainMenuCategory(
        name = name,
        items = items.map { it.toDomain() }
    )
}

fun DomainMenuCategory.toDto(): MenuCategoryDto {
    return MenuCategoryDto(
        name = name,
        items = items.map { it.toDto() }
    )
}

fun MenuItemDto.toDomain(): DomainMenuItem {
    return DomainMenuItem(
        id = id ?: "",
        name = name,
        price = price
    )
}

fun DomainMenuItem.toDto(): MenuItemDto {
    return MenuItemDto(
        id = if (id.isNotBlank()) id else null,
        name = name,
        price = price
    )
}