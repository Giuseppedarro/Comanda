package dev.giuseppedarro.comanda.features.menu.data

import org.jetbrains.exposed.sql.Table

object MenuCategories : Table() {
    val id = varchar("id", 128)
    val name = varchar("name", 100).uniqueIndex()
    val displayOrder = integer("display_order").default(0)

    override val primaryKey = PrimaryKey(id)
}

object MenuItems : Table() {
    val id = varchar("id", 128)
    val categoryId = varchar("category_id", 128).references(MenuCategories.id)
    val name = varchar("name", 200)
    val price = double("price")
    val description = varchar("description", 500).nullable()
    val displayOrder = integer("display_order").default(0)
    val isAvailable = bool("is_available").default(true)

    override val primaryKey = PrimaryKey(id)
}