package dev.giuseppedarro.comanda.features.tables.data.repository

import dev.giuseppedarro.comanda.core.data.repository.toDomainException
import dev.giuseppedarro.comanda.features.tables.data.remote.TableApi
import dev.giuseppedarro.comanda.features.tables.data.remote.dto.toDomain
import dev.giuseppedarro.comanda.features.tables.domain.model.Table
import dev.giuseppedarro.comanda.features.tables.domain.model.TableException
import dev.giuseppedarro.comanda.features.tables.domain.repository.TablesRepository
import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class TablesRepositoryImpl(
    private val tableApi: TableApi
) : TablesRepository {
    override fun getTables(): Flow<Result<List<Table>>> = flow {
        val tableDtos = tableApi.getTables()
        emit(Result.success(tableDtos.map { it.toDomain() }))
    }.catch { e ->
        emit(Result.failure(e.toDomainException()))
    }

    override suspend fun addTable(): Result<Unit> {
        return try {
            tableApi.addTable()
            Result.success(Unit)
        } catch (e: ClientRequestException) {
            val domainError = when (e.response.status) {
                HttpStatusCode.NotFound -> TableException.TableNotFound
                else -> e.toDomainException()
            }
            Result.failure(domainError)
        } catch (e: Exception) {
            Result.failure(e.toDomainException())
        }
    }
}
