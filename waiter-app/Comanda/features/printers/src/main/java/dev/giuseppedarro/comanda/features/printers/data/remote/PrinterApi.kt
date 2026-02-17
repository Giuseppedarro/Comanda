package dev.giuseppedarro.comanda.features.printers.data.remote

import dev.giuseppedarro.comanda.features.printers.data.remote.dto.CreatePrinterRequest
import dev.giuseppedarro.comanda.features.printers.data.remote.dto.PrinterDto
import dev.giuseppedarro.comanda.features.printers.data.remote.dto.UpdatePrinterRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class PrinterApi(private val client: HttpClient) {

    suspend fun getAllPrinters(): List<PrinterDto> {
        return client.get("printers").body()
    }

    suspend fun createPrinter(request: CreatePrinterRequest): PrinterDto {
        return client.post("printers") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun updatePrinter(id: Int, request: UpdatePrinterRequest): PrinterDto {
        return client.put("printers/$id") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun deletePrinter(id: Int) {
        client.delete("printers/$id")
    }
}