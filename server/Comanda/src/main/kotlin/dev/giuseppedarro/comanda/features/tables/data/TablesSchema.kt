package dev.giuseppedarro.comanda.features.tables.data

import org.jetbrains.exposed.sql.Table

object Tables : Table() {
    val number = integer("number").uniqueIndex()
    val isOccupied = bool("is_occupied").default(false)

    override val primaryKey = PrimaryKey(number)
}
