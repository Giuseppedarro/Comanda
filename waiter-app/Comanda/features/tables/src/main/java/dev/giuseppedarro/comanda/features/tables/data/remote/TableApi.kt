package dev.giuseppedarro.comanda.features.tables.data.remote

import dev.giuseppedarro.comanda.features.tables.data.remote.dto.TableDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable

class TableApi(private val client: HttpClient) {

    suspend fun getTables(): List<TableDto> {
        return client.get("tables").body()
    }

    suspend fun addTable(number: Int? = null): HttpResponse {
        return client.post("tables") {
            contentType(ContentType.Application.Json)
            if (number != null) {
                setBody(AddTableRequest(number))
            } else {
                setBody(emptyMap<String, Int>())
            }
        }
    }
}

@Serializable
data class AddTableRequest(val number: Int)
