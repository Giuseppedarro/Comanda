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
                it[Users.employeeId] = "1234"
                it[Users.name] = "Default Waiter"
                it[Users.password] = "password"
                it[Users.role] = "WAITER"
            }
        }

        // Create default tables if none exist
        if (Tables.selectAll().count() == 0L) {
            for (i in 1..20) {
                Tables.insert {
                    it[Tables.number] = i
                    it[Tables.isOccupied] = false // Default to free
                }
            }
        }

        // Create default menu if none exists
        if (MenuCategories.selectAll().count() == 0L) {
            
            // --- STARTERS ---
            val catStarters = "cat_starters"
            MenuCategories.insert { 
                it[MenuCategories.id] = catStarters
                it[MenuCategories.name] = "Starters" 
            }
            val starters = listOf(
                "Bruschetta" to 7.00, "Garlic Bread" to 5.00, "Caprese Salad" to 9.50,
                "Fried Calamari" to 11.00, "Stuffed Mushrooms" to 8.50, "Chicken Wings" to 9.00,
                "Mozzarella Sticks" to 7.50, "Onion Rings" to 6.00, "Shrimp Cocktail" to 12.00,
                "Soup of the Day" to 6.50
            )
            starters.forEachIndexed { i, (itemName, itemPrice) ->
                MenuItems.insert { 
                    it[MenuItems.id] = "s_$i"
                    it[MenuItems.categoryId] = catStarters
                    it[MenuItems.name] = itemName
                    it[MenuItems.price] = itemPrice
                }
            }

            // --- PASTA ---
            val catPasta = "cat_pasta"
            MenuCategories.insert { 
                it[MenuCategories.id] = catPasta
                it[MenuCategories.name] = "Pasta" 
            }
            val pastas = listOf(
                "Spaghetti Carbonara" to 13.50, "Penne Arrabbiata" to 11.00, "Lasagna" to 14.00,
                "Fettuccine Alfredo" to 12.50, "Spaghetti Bolognese" to 13.00, "Ravioli Ricotta" to 14.50,
                "Pesto Gnocchi" to 12.00, "Seafood Linguine" to 16.00, "Mac & Cheese" to 10.50,
                "Truffle Risotto" to 15.50
            )
            pastas.forEachIndexed { i, (itemName, itemPrice) ->
                MenuItems.insert { 
                    it[MenuItems.id] = "p_$i"
                    it[MenuItems.categoryId] = catPasta
                    it[MenuItems.name] = itemName
                    it[MenuItems.price] = itemPrice
                }
            }

            // --- PIZZA ---
            val catPizza = "cat_pizza"
            MenuCategories.insert { 
                it[MenuCategories.id] = catPizza
                it[MenuCategories.name] = "Pizza" 
            }
            val pizzas = listOf(
                "Margherita" to 9.00, "Pepperoni" to 11.00, "Hawaiian" to 11.50,
                "Four Cheese" to 12.00, "Vegetarian" to 10.50, "BBQ Chicken" to 13.00,
                "Meat Lovers" to 14.00, "Mushroom & Truffle" to 13.50, "Buffalo Chicken" to 12.50,
                "Calzone" to 11.00
            )
            pizzas.forEachIndexed { i, (itemName, itemPrice) ->
                MenuItems.insert { 
                    it[MenuItems.id] = "z_$i"
                    it[MenuItems.categoryId] = catPizza
                    it[MenuItems.name] = itemName
                    it[MenuItems.price] = itemPrice
                }
            }

            // --- MAINS ---
            val catMains = "cat_mains"
            MenuCategories.insert { 
                it[MenuCategories.id] = catMains
                it[MenuCategories.name] = "Main Courses" 
            }
            val mains = listOf(
                "Gourmet Burger" to 14.50, "Grilled Salmon" to 18.00, "Ribeye Steak" to 24.00,
                "Chicken Parmigiana" to 16.50, "Fish and Chips" to 15.00, "Lamb Chops" to 22.00,
                "BBQ Ribs" to 19.50, "Roast Chicken" to 15.50, "Beef Stew" to 16.00,
                "Vegetable Curry" to 13.50
            )
            mains.forEachIndexed { i, (itemName, itemPrice) ->
                MenuItems.insert { 
                    it[MenuItems.id] = "m_$i"
                    it[MenuItems.categoryId] = catMains
                    it[MenuItems.name] = itemName
                    it[MenuItems.price] = itemPrice
                }
            }

            // --- SIDES ---
            val catSides = "cat_sides"
            MenuCategories.insert { 
                it[MenuCategories.id] = catSides
                it[MenuCategories.name] = "Sides" 
            }
            val sides = listOf(
                "French Fries" to 4.00, "Sweet Potato Fries" to 5.00, "Mashed Potatoes" to 4.50,
                "Steamed Vegetables" to 5.00, "Coleslaw" to 3.50, "Side Salad" to 4.00,
                "Garlic Spinach" to 5.00, "Rice Pilaf" to 4.00, "Corn on the Cob" to 3.50,
                "Roasted Potatoes" to 4.50
            )
            sides.forEachIndexed { i, (itemName, itemPrice) ->
                MenuItems.insert { 
                    it[MenuItems.id] = "sd_$i"
                    it[MenuItems.categoryId] = catSides
                    it[MenuItems.name] = itemName
                    it[MenuItems.price] = itemPrice
                }
            }

            // --- DESSERTS ---
            val catDesserts = "cat_desserts"
            MenuCategories.insert { 
                it[MenuCategories.id] = catDesserts
                it[MenuCategories.name] = "Desserts" 
            }
            val desserts = listOf(
                "Tiramisu" to 7.00, "Cheesecake" to 6.50, "Chocolate Lava Cake" to 8.00,
                "Apple Pie" to 6.00, "Ice Cream Sundae" to 5.50, "Panna Cotta" to 6.50,
                "Fruit Salad" to 5.00, "Brownie" to 4.50, "Creme Brulee" to 7.50,
                "Lemon Sorbet" to 4.00
            )
            desserts.forEachIndexed { i, (itemName, itemPrice) ->
                MenuItems.insert { 
                    it[MenuItems.id] = "d_$i"
                    it[MenuItems.categoryId] = catDesserts
                    it[MenuItems.name] = itemName
                    it[MenuItems.price] = itemPrice
                }
            }

            // --- DRINKS ---
            val catDrinks = "cat_drinks"
            MenuCategories.insert { 
                it[MenuCategories.id] = catDrinks
                it[MenuCategories.name] = "Drinks" 
            }
            val drinks = listOf(
                "Coke" to 3.00, "Diet Coke" to 3.00, "Sprite" to 3.00,
                "Fanta" to 3.00, "Iced Tea" to 3.50, "Lemonade" to 3.50,
                "Sparkling Water" to 2.50, "Still Water" to 2.00, "Orange Juice" to 4.00,
                "Apple Juice" to 4.00
            )
            drinks.forEachIndexed { i, (itemName, itemPrice) ->
                MenuItems.insert { 
                    it[MenuItems.id] = "dr_$i"
                    it[MenuItems.categoryId] = catDrinks
                    it[MenuItems.name] = itemName
                    it[MenuItems.price] = itemPrice
                }
            }
            
            // --- ALCOHOL ---
            val catAlcohol = "cat_alcohol"
            MenuCategories.insert { 
                it[MenuCategories.id] = catAlcohol
                it[MenuCategories.name] = "Alcohol" 
            }
            val alcohol = listOf(
                "Draft Beer" to 5.50, "Craft IPA" to 7.00, "Red Wine (Glass)" to 8.00,
                "White Wine (Glass)" to 8.00, "Prosecco" to 9.00, "Gin & Tonic" to 10.00,
                "Mojito" to 11.00, "Old Fashioned" to 12.00, "Margarita" to 11.00,
                "Whiskey Sour" to 11.50
            )
            alcohol.forEachIndexed { i, (itemName, itemPrice) ->
                MenuItems.insert { 
                    it[MenuItems.id] = "a_$i"
                    it[MenuItems.categoryId] = catAlcohol
                    it[MenuItems.name] = itemName
                    it[MenuItems.price] = itemPrice
                }
            }
        }
    }
}
