package dev.giuseppedarro.comanda.features.settings.data.remote

import dev.giuseppedarro.comanda.features.settings.data.remote.dto.CreateUserRequest
import dev.giuseppedarro.comanda.features.settings.data.remote.dto.UserResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class UserApi(private val httpClient: HttpClient) {

    suspend fun createUser(request: CreateUserRequest): Result<UserResponse> {
        return try {
            val response = httpClient.post("/users") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }
            Result.success(response.body())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}