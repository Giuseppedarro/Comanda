package dev.giuseppedarro.comanda.features.menu.data.repository

import dev.giuseppedarro.comanda.features.menu.data.MenuCategories
import dev.giuseppedarro.comanda.features.menu.data.MenuItems
import dev.giuseppedarro.comanda.features.menu.domain.model.MenuItem
import dev.giuseppedarro.comanda.features.menu.domain.model.MenuCategory
import dev.giuseppedarro.comanda.features.menu.domain.repository.MenuRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class MenuRepositoryImpl : MenuRepository {
    override suspend fun getMenu(): List<MenuCategory> = transaction {
        val categories = MenuCategories.selectAll()
            .orderBy(MenuCategories.displayOrder to SortOrder.ASC)
            .map { row ->
                MenuCategory(
                    id = row[MenuCategories.id],
                    name = row[MenuCategories.name],
                    items = emptyList() // Will be filled in the next query
                )
            }

        val itemsByCategory = MenuItems.select { MenuItems.isAvailable eq true }
            .orderBy(MenuItems.categoryId to SortOrder.ASC, MenuItems.displayOrder to SortOrder.ASC)
            .groupBy { it[MenuItems.categoryId] }

        categories.map { category ->
            category.copy(
                items = itemsByCategory[category.id]?.map { row ->
                    MenuItem(
                        id = row[MenuItems.id],
                        categoryId = row[MenuItems.categoryId],
                        name = row[MenuItems.name],
                        price = row[MenuItems.price],
                        description = row[MenuItems.description],
                        isAvailable = row[MenuItems.isAvailable]
                    )
                } ?: emptyList()
            )
        }
    }
}