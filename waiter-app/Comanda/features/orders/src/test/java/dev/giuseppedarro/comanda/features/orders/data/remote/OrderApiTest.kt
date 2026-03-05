package dev.giuseppedarro.comanda.features.orders.data.remote

import com.google.common.truth.Truth.assertThat
import dev.giuseppedarro.comanda.features.orders.data.remote.dto.SubmitOrderRequest
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Test

class OrderApiTest {

    @Test
    fun getOrdersForTable_success() = runTest {
        // Given
        val tableNumber = 1
        val jsonBody = """{"tableNumber":1,"numberOfPeople":2,"status":"Open","items":[],"createdAt":"2024-01-01T12:00:00Z"}"""
        val mockEngine = MockEngine { request ->
            respond(
                content = jsonBody,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val client = createClient(mockEngine)
        val orderApi = OrderApi(client)

        // When
        val order = orderApi.getOrdersForTable(tableNumber)

        // Then
        assertThat(order).isNotNull()
        assertThat(order?.tableNumber).isEqualTo(tableNumber)
        val executedRequest = mockEngine.requestHistory.first()
        assertThat(executedRequest.url.encodedPath).isEqualTo("/orders/1")
    }

    @Test
    fun submitOrder_success() = runTest {
        // Given
        val requestBody = SubmitOrderRequest(1, 2, emptyList())
        val mockEngine = MockEngine { request ->
            respond(
                content = "",
                status = HttpStatusCode.OK
            )
        }
        val client = createClient(mockEngine)
        val orderApi = OrderApi(client)

        // When
        orderApi.submitOrder(requestBody)

        // Then
        val executedRequest = mockEngine.requestHistory.first()
        assertThat(executedRequest.method).isEqualTo(HttpMethod.Post)
        assertThat(executedRequest.url.encodedPath).isEqualTo("/orders")
    }

    private fun createClient(mockEngine: MockEngine): HttpClient {
        return HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }
            defaultRequest {
                url("http://localhost/")
            }
        }
    }
}
