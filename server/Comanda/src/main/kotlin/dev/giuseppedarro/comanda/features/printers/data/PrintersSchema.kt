package dev.giuseppedarro.comanda.features.printers.data

import org.jetbrains.exposed.sql.Table

object Printers : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 100)
    val address = varchar("address", 255)
    val port = integer("port")

    override val primaryKey = PrimaryKey(id)
}