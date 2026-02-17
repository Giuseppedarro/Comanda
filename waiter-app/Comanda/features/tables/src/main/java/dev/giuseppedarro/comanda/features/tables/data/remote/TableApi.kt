package dev.giuseppedarro.comanda.features.tables.data.remote

import dev.giuseppedarro.comanda.features.tables.data.remote.dto.TableDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post

class TableApi(private val client: HttpClient) {

    suspend fun getTables(): List<TableDto> {
        return client.get("tables").body()
    }

    suspend fun addTable(): TableDto {
        return client.post("tables").body()
    }
}