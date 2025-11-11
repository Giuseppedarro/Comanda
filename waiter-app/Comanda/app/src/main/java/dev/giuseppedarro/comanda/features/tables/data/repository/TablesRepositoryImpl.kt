package dev.giuseppedarro.comanda.features.tables.data.repository

import dev.giuseppedarro.comanda.core.domain.TokenRepository
import dev.giuseppedarro.comanda.core.utils.Result
import dev.giuseppedarro.comanda.features.tables.domain.model.Table
import dev.giuseppedarro.comanda.features.tables.domain.repository.TablesRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TablesRepositoryImpl(
    private val client: HttpClient,
    private val tokenRepository: TokenRepository
) : TablesRepository {
    override fun getTables(): Flow<List<Table>> = flow {
        try {
            val token = tokenRepository.getAccessToken()
            if (token == null) {
                throw Exception("User not authenticated")
            }

            val tables = client.get("tables") {
                headers {
                    append("Authorization", "Bearer $token")
                }
            }.body<List<Table>>()
            
            emit(tables)
        } catch (e: Exception) {
            e.printStackTrace()
            emit(emptyList()) // Emit an empty list on error to avoid crashing the UI
        }
    }

    override suspend fun addTable(): Result<Unit> {
        return try {
            val token = tokenRepository.getAccessToken()
            if (token == null) {
                return Result.Error("User not authenticated")
            }

            client.post("tables") {
                headers {
                    append("Authorization", "Bearer $token")
                }
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e.message ?: "An unknown error occurred")
        }
    }
}
