package dev.giuseppedarro.comanda.core.domain

/**
 * Domain-facing contract for working with auth tokens.
 * Implementations should securely persist tokens and provide access to them.
 */
interface TokenRepository {
    suspend fun saveTokens(access: String, refresh: String)
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun clear()
}