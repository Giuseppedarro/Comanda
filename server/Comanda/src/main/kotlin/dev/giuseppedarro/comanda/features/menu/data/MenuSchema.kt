package dev.giuseppedarro.comanda.features.menu.data

import org.jetbrains.exposed.sql.Table

object MenuCategories : Table("menu_categories") {
    val id = varchar("id", 128)
    val name = varchar("name", 128)

    override val primaryKey = PrimaryKey(id)
}

object MenuItems : Table("menu_items") {
    val id = varchar("id", 128)
    val categoryId = varchar("category_id", 128) references MenuCategories.id
    val name = varchar("name", 128)
    val price = double("price")

    override val primaryKey = PrimaryKey(id)
}
