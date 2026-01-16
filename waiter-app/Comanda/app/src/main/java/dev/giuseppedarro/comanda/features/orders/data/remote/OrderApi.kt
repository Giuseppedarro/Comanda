package dev.giuseppedarro.comanda.features.orders.data.remote

import dev.giuseppedarro.comanda.features.orders.data.remote.dto.GetOrderResponse
import dev.giuseppedarro.comanda.features.orders.data.remote.dto.MenuCategoryDto
import dev.giuseppedarro.comanda.features.orders.data.remote.dto.SubmitOrderRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class OrderApi(private val client: HttpClient) {

    suspend fun getMenu(): List<MenuCategoryDto> {
        return client.get("menu").body()
    }

    suspend fun getOrdersForTable(tableNumber: Int): GetOrderResponse? {
        return try {
            client.get("orders/$tableNumber").body()
        } catch (e: ClientRequestException) {
            // Handle 404 Not Found - return null when no order exists for the table
            if (e.response.status == HttpStatusCode.NotFound) {
                null
            } else {
                throw e
            }
        }
    }

    suspend fun submitOrder(request: SubmitOrderRequest) {
        client.post("orders") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }
}