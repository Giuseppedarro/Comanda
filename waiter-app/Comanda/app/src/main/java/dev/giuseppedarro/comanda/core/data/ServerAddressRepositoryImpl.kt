package dev.giuseppedarro.comanda.core.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.giuseppedarro.comanda.core.domain.ServerAddressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ServerAddressRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : ServerAddressRepository {

    private object PreferencesKeys {
        val SERVER_ADDRESS = stringPreferencesKey("server_address")
    }

    override fun getAddress(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.SERVER_ADDRESS] ?: "10.0.2.2:8080"
        }
    }

    override suspend fun setAddress(address: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SERVER_ADDRESS] = address
        }
    }
}
