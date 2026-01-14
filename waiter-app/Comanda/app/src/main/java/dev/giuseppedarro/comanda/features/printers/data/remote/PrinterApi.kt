package dev.giuseppedarro.comanda.features.printers.data.remote

import dev.giuseppedarro.comanda.features.printers.data.remote.dto.CreatePrinterRequest
import dev.giuseppedarro.comanda.features.printers.data.remote.dto.PrinterDto
import dev.giuseppedarro.comanda.features.printers.data.remote.dto.UpdatePrinterRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class PrinterApi(private val client: HttpClient) {

    suspend fun getAllPrinters(token: String): List<PrinterDto> {
        return client.get("printers") {
            headers {
                append("Authorization", "Bearer $token")
            }
        }.body()
    }

    suspend fun createPrinter(token: String, request: CreatePrinterRequest): PrinterDto {
        return client.post("printers") {
            headers {
                append("Authorization", "Bearer $token")
            }
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun updatePrinter(token: String, id: Int, request: UpdatePrinterRequest): PrinterDto {
        return client.put("printers/$id") {
            headers {
                append("Authorization", "Bearer $token")
            }
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun deletePrinter(token: String, id: Int) {
        client.delete("printers/$id") {
            headers {
                append("Authorization", "Bearer $token")
            }
        }
    }
}