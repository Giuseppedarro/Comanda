package dev.giuseppedarro.comanda.core.utils

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.util.Base64

class JwtDecoder(private val json: Json) {

    @Serializable
    private data class JwtPayload(
        val id: Int? = null,
        val sub: String? = null,
        val name: String? = null,
        val role: String? = null
    )

    fun getUserId(token: String): Int? {
        return try {
            val parts = token.split(".")
            if (parts.size < 2) return null
            
            val payloadBase64 = parts[1]
            val decodedBytes = Base64.getUrlDecoder().decode(payloadBase64)
            val decodedString = String(decodedBytes)
            
            val payload = json.decodeFromString<JwtPayload>(decodedString)
            payload.id
        } catch (e: Exception) {
            null
        }
    }
}
