package dev.giuseppedarro.comanda.features.menu.data.remote

import dev.giuseppedarro.comanda.features.menu.data.remote.dto.CreateMenuItemRequest
import dev.giuseppedarro.comanda.features.menu.data.remote.dto.MenuCategoryDto
import dev.giuseppedarro.comanda.features.menu.data.remote.dto.MenuItemDto
import dev.giuseppedarro.comanda.features.menu.data.remote.dto.UpdateMenuItemRequest
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

    suspend fun createCategory(categoryDto: MenuCategoryDto) {
        client.post("menu/categories") {
            contentType(ContentType.Application.Json)
            setBody(categoryDto)
        }
    }

    suspend fun updateCategory(categoryId: String, categoryDto: MenuCategoryDto) {
        client.put("menu/categories/$categoryId") {
            contentType(ContentType.Application.Json)
            setBody(categoryDto)
        }
    }

    suspend fun deleteCategory(categoryId: String) {
        client.delete("menu/categories/$categoryId")
    }

    suspend fun addMenuItem(categoryId: String, itemDto: CreateMenuItemRequest) {
        client.post("menu/categories/$categoryId/items") {
            contentType(ContentType.Application.Json)
            setBody(itemDto)
        }
    }

    suspend fun updateMenuItem(itemId: String, itemDto: UpdateMenuItemRequest) {
        client.put("menu/items/$itemId") {
            contentType(ContentType.Application.Json)
            setBody(itemDto)
        }
    }

    suspend fun deleteMenuItem(itemId: String) {
        client.delete("menu/items/$itemId")
    }
}