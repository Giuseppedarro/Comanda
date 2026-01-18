package dev.giuseppedarro.comanda.features.menu.data.remote

import dev.giuseppedarro.comanda.features.menu.data.remote.dto.MenuItemDto
import dev.giuseppedarro.comanda.features.menu.data.remote.dto.MenuCategoryDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class MenuApi(private val client: HttpClient) {

    suspend fun getMenu(): List<MenuCategoryDto> {
        return client.get("menu").body()
    }

    suspend fun addMenuItem(categoryName: String, itemDto: MenuItemDto) {
        client.post("menu/categories/$categoryName/items") {
            contentType(ContentType.Application.Json)
            setBody(itemDto)
        }
    }

    suspend fun updateMenuItem(categoryName: String, itemId: String, itemDto: MenuItemDto) {
        client.put("menu/categories/$categoryName/items/$itemId") {
            contentType(ContentType.Application.Json)
            setBody(itemDto)
        }
    }

    suspend fun deleteMenuItem(categoryName: String, itemId: String) {
        client.delete("menu/categories/$categoryName/items/$itemId")
    }
}