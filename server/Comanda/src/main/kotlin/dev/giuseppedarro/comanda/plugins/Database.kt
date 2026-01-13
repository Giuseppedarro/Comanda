package dev.giuseppedarro.comanda.plugins

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
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
        SchemaUtils.create(Users, Tables, Orders, OrderItems, Printers)

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
    }
}
