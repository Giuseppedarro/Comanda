package dev.giuseppedarro.comanda.core.network

import dev.giuseppedarro.comanda.core.domain.model.UserProfile
import dev.giuseppedarro.comanda.core.network.dto.CreateUserRequest
import dev.giuseppedarro.comanda.core.network.dto.UpdateUserRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class UserApi(private val client: HttpClient) {

    suspend fun getUserProfile(id: Int): UserProfile {
        return client.get("users/$id").body()
    }

    suspend fun createUser(request: CreateUserRequest): UserProfile {
        return client.post("users") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun getUsers(): List<UserProfile> {
        return client.get("users").body()
    }

    suspend fun updateUser(id: Int, request: UpdateUserRequest): UserProfile {
        return client.put("users/$id") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun deleteUser(id: Int) {
        client.delete("users/$id")
    }
}
