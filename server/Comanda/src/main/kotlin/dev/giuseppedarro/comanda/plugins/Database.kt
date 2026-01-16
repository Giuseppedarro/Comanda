package dev.giuseppedarro.comanda.plugins

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import dev.giuseppedarro.comanda.features.menu.data.MenuCategories
import dev.giuseppedarro.comanda.features.menu.data.MenuItems
import dev.giuseppedarro.comanda.features.orders.data.OrderItems
import dev.giuseppedarro.comanda.features.orders.data.Orders
import dev.giuseppedarro.comanda.features.printers.data.Printers
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
        SchemaUtils.create(Users, Tables, Orders, OrderItems, Printers, MenuCategories, MenuItems)

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

        // Create default printers if none exist
        if (Printers.selectAll().count() == 0L) {
            Printers.insert {
                it[name] = "Kitchen"
                it[address] = "192.168.1.100"
                it[port] = 9100
            }
            Printers.insert {
                it[name] = "Bar"
                it[address] = "192.168.1.101"
                it[port] = 9100
            }
            Printers.insert {
                it[name] = "Cashier"
                it[address] = "192.168.1.102"
                it[port] = 9100
            }
        }

        // Create default menu if none exists
        if (MenuCategories.selectAll().count() == 0L) {
            // Create categories
            val categories = listOf(
                "appetizers" to "Appetizers",
                "main_courses" to "Main Courses",
                "desserts" to "Desserts",
                "drinks" to "Drinks"
            )

            categories.forEachIndexed { index, (id, name) ->
                MenuCategories.insert {
                    it[MenuCategories.id] = id
                    it[MenuCategories.name] = name
                    it[MenuCategories.displayOrder] = index
                }
            }

            // Create appetizer items
            val appetizers = listOf(
                "app_bruschetta" to 7.00,
                "app_garlic_bread" to 5.00,
                "app_mushrooms" to 8.50,
                "app_spring_rolls" to 6.00,
                "app_onion_rings" to 5.50,
                "app_calamari" to 9.00
            )
            appetizers.forEachIndexed { index, (id, price) ->
                MenuItems.insert {
                    it[MenuItems.id] = id
                    it[MenuItems.categoryId] = "appetizers"
                    it[MenuItems.name] = id.removePrefix("app_")
                        .split("_")
                        .joinToString(" ") { it.replaceFirstChar { char -> char.uppercaseChar() } }
                    it[MenuItems.price] = price
                    it[MenuItems.displayOrder] = index
                }
            }

            // Create main course items
            val mainCourses = listOf(
                "main_burger" to 12.99,
                "main_caesar_salad" to 8.50
            )
            mainCourses.forEachIndexed { index, (id, price) ->
                MenuItems.insert {
                    it[MenuItems.id] = id
                    it[MenuItems.categoryId] = "main_courses"
                    it[MenuItems.name] = id.removePrefix("main_")
                        .split("_")
                        .joinToString(" ") { it.replaceFirstChar { char -> char.uppercaseChar() } }
                    it[MenuItems.price] = price
                    it[MenuItems.displayOrder] = index
                }
            }

            // Create dessert items
            val desserts = listOf(
                "dess_tiramisu" to 6.50,
                "dess_cheesecake" to 7.50
            )
            desserts.forEachIndexed { index, (id, price) ->
                MenuItems.insert {
                    it[MenuItems.id] = id
                    it[MenuItems.categoryId] = "desserts"
                    it[MenuItems.name] = id.removePrefix("dess_")
                        .replaceFirstChar { char -> char.uppercaseChar() }
                    it[MenuItems.price] = price
                    it[MenuItems.displayOrder] = index
                }
            }

            // Create drink items
            val drinks = listOf(
                "drink_cola" to 2.50,
                "drink_cappuccino" to 4.75,
                "drink_iced_tea" to 2.00,
                "drink_orange_juice" to 3.00,
                "drink_latte" to 4.00,
                "drink_water" to 1.00,
                "drink_espresso" to 3.00,
                "drink_lemonade" to 3.50,
                "drink_apple_juice" to 3.00,
                "drink_sparkling_water" to 1.50,
                "drink_green_tea" to 2.50,
                "drink_beer" to 5.00
            )
            drinks.forEachIndexed { index, (id, price) ->
                MenuItems.insert {
                    it[MenuItems.id] = id
                    it[MenuItems.categoryId] = "drinks"
                    it[MenuItems.name] = id.removePrefix("drink_")
                        .split("_")
                        .joinToString(" ") { it.replaceFirstChar { char -> char.uppercaseChar() } }
                    it[MenuItems.price] = price
                    it[MenuItems.displayOrder] = index
                }
            }
        }
    }
}
