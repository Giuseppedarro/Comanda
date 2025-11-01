package dev.giuseppedarro.comanda.core.domain

import kotlinx.coroutines.flow.Flow

interface ServerAddressRepository {
    fun getAddress(): Flow<String>
    suspend fun setAddress(address: String)
}
