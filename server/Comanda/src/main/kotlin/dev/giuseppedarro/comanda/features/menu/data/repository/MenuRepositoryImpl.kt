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

    // Category CRUD
    override suspend fun addCategory(category: MenuCategory): Result<Unit> = try {
        transaction {
            MenuCategories.insert {
                it[id] = category.id
                it[name] = category.name
                it[displayOrder] = 0 // Default or from model if added
            }
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun updateCategory(category: MenuCategory): Result<Unit> = try {
        transaction {
            MenuCategories.update({ MenuCategories.id eq category.id }) {
                it[name] = category.name
                // Update other fields if added to model
            }
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun deleteCategory(categoryId: String): Result<Unit> = try {
        transaction {
            // Items must be deleted first due to FK constraint, or handled by ON DELETE CASCADE in DB
            // Assuming manual handling for safety here:
            MenuItems.deleteWhere { MenuItems.categoryId eq categoryId }
            MenuCategories.deleteWhere { MenuCategories.id eq categoryId }
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getCategory(categoryId: String): Result<MenuCategory> = try {
        transaction {
            val categoryRow = MenuCategories.select { MenuCategories.id eq categoryId }.singleOrNull()
                ?: throw IllegalArgumentException("Category not found")

            val items = MenuItems.select { MenuItems.categoryId eq categoryId }
                .map { row ->
                    MenuItem(
                        id = row[MenuItems.id],
                        categoryId = row[MenuItems.categoryId],
                        name = row[MenuItems.name],
                        price = row[MenuItems.price],
                        description = row[MenuItems.description],
                        isAvailable = row[MenuItems.isAvailable]
                    )
                }

            Result.success(
                MenuCategory(
                    id = categoryRow[MenuCategories.id],
                    name = categoryRow[MenuCategories.name],
                    items = items
                )
            )
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    // Item CRUD
    override suspend fun addItem(item: MenuItem): Result<Unit> = try {
        transaction {
            MenuItems.insert {
                it[id] = item.id
                it[categoryId] = item.categoryId
                it[name] = item.name
                it[price] = item.price
                it[description] = item.description
                it[isAvailable] = item.isAvailable
                it[displayOrder] = 0
            }
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun updateItem(item: MenuItem): Result<Unit> = try {
        transaction {
            MenuItems.update({ MenuItems.id eq item.id }) {
                it[categoryId] = item.categoryId
                it[name] = item.name
                it[price] = item.price
                it[description] = item.description
                it[isAvailable] = item.isAvailable
            }
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun deleteItem(itemId: String): Result<Unit> = try {
        transaction {
            MenuItems.deleteWhere { MenuItems.id eq itemId }
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getItem(itemId: String): Result<MenuItem> = try {
        transaction {
            val row = MenuItems.select { MenuItems.id eq itemId }.singleOrNull()
                ?: throw IllegalArgumentException("Item not found")

            Result.success(
                MenuItem(
                    id = row[MenuItems.id],
                    categoryId = row[MenuItems.categoryId],
                    name = row[MenuItems.name],
                    price = row[MenuItems.price],
                    description = row[MenuItems.description],
                    isAvailable = row[MenuItems.isAvailable]
                )
            )
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}