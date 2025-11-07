package dev.giuseppedarro.comanda.features.orders.data.remote

import dev.giuseppedarro.comanda.features.orders.data.remote.dto.GetOrderResponse
import dev.giuseppedarro.comanda.features.orders.data.remote.dto.SubmitOrderRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class OrderApi(private val client: HttpClient) {

    suspend fun getOrdersForTable(token: String, tableNumber: Int): List<GetOrderResponse> {
        return client.get("orders") {
            headers {
                append("Authorization", "Bearer $token")
            }
            url { parameters.append("tableNumber", tableNumber.toString()) }
        }.body()
    }

    suspend fun submitOrder(token: String, request: SubmitOrderRequest) {
        client.post("orders") {
            headers {
                append("Authorization", "Bearer $token")
            }
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }
}
