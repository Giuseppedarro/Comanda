package dev.giuseppedarro.comanda.features.users.data

import org.jetbrains.exposed.sql.Table

object Users : Table() {
    val id = integer("id").autoIncrement()
    val employeeId = varchar("employee_id", 128).uniqueIndex()
    val name = varchar("name", 256)
    val password = varchar("password", 256) //
    val role = varchar("role", 64)

    override val primaryKey = PrimaryKey(id)
}
