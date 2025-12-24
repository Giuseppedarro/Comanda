package dev.giuseppedarro.comanda.features.orders.data

import org.jetbrains.exposed.sql.Table

object Orders : Table() {
    val id = varchar("id", 128)
    val tableNumber = integer("table_number")
    val numberOfPeople = integer("number_of_people")
    val status = varchar("status", 32) // "open", "served", "closed"
    val createdAt = varchar("created_at", 64)
    val subtotal = double("subtotal").nullable()
    val serviceCharge = double("service_charge").nullable()
    val total = double("total").nullable()

    override val primaryKey = PrimaryKey(id)
}

object OrderItems : Table() {
    val id = varchar("id", 128)
    val orderId = varchar("order_id", 128) references Orders.id
    val itemId = varchar("item_id", 128)
    val quantity = integer("quantity")
    val notes = varchar("notes", 256).nullable()

    override val primaryKey = PrimaryKey(id)
}
