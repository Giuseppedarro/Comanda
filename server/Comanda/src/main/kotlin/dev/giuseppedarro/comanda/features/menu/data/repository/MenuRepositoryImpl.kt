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

    override suspend fun updateMenu(categories: List<MenuCategory>): Result<Unit> = try {
        transaction {
            // Delete all existing menu items first (due to foreign key constraint)
            MenuItems.deleteAll()
            // Delete all existing categories
            MenuCategories.deleteAll()

            // Insert new categories and items
            for (category in categories) {
                MenuCategories.insert {
                    it[id] = category.id
                    it[name] = category.name
                    it[displayOrder] = 0 // Could be extracted from update DTO if needed
                }

                for (item in category.items) {
                    MenuItems.insert {
                        it[this.id] = item.id
                        it[categoryId] = item.categoryId
                        it[name] = item.name
                        it[price] = item.price
                        it[description] = item.description
                        it[isAvailable] = item.isAvailable
                        it[displayOrder] = 0 // Could be extracted from update DTO if needed
                    }
                }
            }
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}