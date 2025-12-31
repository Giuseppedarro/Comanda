package dev.giuseppedarro.comanda.plugins

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import dev.giuseppedarro.comanda.features.menu.data.MenuCategories
import dev.giuseppedarro.comanda.features.menu.data.MenuItems
import dev.giuseppedarro.comanda.features.orders.data.OrderItems
import dev.giuseppedarro.comanda.features.orders.data.Orders
import dev.giuseppedarro.comanda.features.tables.data.Tables
import dev.giuseppedarro.comanda.features.users.data.Users
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun Application.configureDatabase() {
    val config = HikariConfig().apply {
        driverClassName = "org.postgresql.Driver"
        jdbcUrl = "jdbc:postgresql://localhost:5432/comanda"
        username = "postgres"
        password = "postgres"
        maximumPoolSize = 3
        isAutoCommit = false
        transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        validate()
    }
    val dataSource = HikariDataSource(config)
    Database.connect(dataSource)

    transaction {
        SchemaUtils.create(Users, Tables, Orders, OrderItems, MenuCategories, MenuItems)

        // Create a default user if none exists
        if (Users.selectAll().count() == 0L) {
            Users.insert {
                it[employeeId] = "1234"
                it[name] = "Default Waiter"
                it[password] = "password"
                it[role] = "WAITER"
            }
        }

        // Create default tables if none exist
        if (Tables.selectAll().count() == 0L) {
            for (i in 1..20) {
                Tables.insert {
                    it[number] = i
                    it[isOccupied] = false // Default to free
                }
            }
        }

        // Create default menu if none exists
        if (MenuCategories.selectAll().count() == 0L) {
            // Appetizers
            MenuCategories.insert {
                it[id] = "cat_appetizers"
                it[name] = "Appetizers"
            }
            MenuItems.insert {
                it[id] = "1"
                it[categoryId] = "cat_appetizers"
                it[name] = "Bruschetta"
                it[price] = 7.00
            }
            MenuItems.insert {
                it[id] = "2"
                it[categoryId] = "cat_appetizers"
                it[name] = "Garlic Bread"
                it[price] = 5.00
            }

            // Main Courses
            MenuCategories.insert {
                it[id] = "cat_main"
                it[name] = "Main Courses"
            }
            MenuItems.insert {
                it[id] = "3"
                it[categoryId] = "cat_main"
                it[name] = "Gourmet Burger"
                it[price] = 12.99
            }
            MenuItems.insert {
                it[id] = "4"
                it[categoryId] = "cat_main"
                it[name] = "Caesar Salad"
                it[price] = 8.50
            }
            
            // Drinks
            MenuCategories.insert {
                it[id] = "cat_drinks"
                it[name] = "Drinks"
            }
            MenuItems.insert {
                it[id] = "5"
                it[categoryId] = "cat_drinks"
                it[name] = "Coke"
                it[price] = 3.00
            }
            MenuItems.insert {
                it[id] = "6"
                it[categoryId] = "cat_drinks"
                it[name] = "Beer"
                it[price] = 5.00
            }
        }
    }
}
