package dev.giuseppedarro.comanda.core.data.repository

import android.util.Base64
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.giuseppedarro.comanda.core.data.TinkManager
import dev.giuseppedarro.comanda.core.domain.repository.TokenRepository
import kotlinx.coroutines.flow.first

class TokenRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
    private val tinkManager: TinkManager,
) : TokenRepository {

    override suspend fun saveTokens(access: String, refresh: String) {
        val encAccess = encrypt(access)
        val encRefresh = encrypt(refresh)
        dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN] = encAccess
            prefs[REFRESH_TOKEN] = encRefresh
        }
    }

    override suspend fun getAccessToken(): String? {
        val prefs = dataStore.data.first()
        val enc = prefs[ACCESS_TOKEN] ?: return null
        return decrypt(enc)
    }

    override suspend fun getRefreshToken(): String? {
        val prefs = dataStore.data.first()
        val enc = prefs[REFRESH_TOKEN] ?: return null
        return decrypt(enc)
    }

    override suspend fun clear() {
        dataStore.edit { it.clear() }
    }

    override suspend fun saveAccessToken(token: String) {
        val enc = encrypt(token)
        dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN] = enc
        }
    }

    private fun encrypt(value: String): String {
        val encryptedBytes = tinkManager.encrypt(value)
        return Base64.encodeToString(encryptedBytes, Base64.NO_WRAP)
    }

    private fun decrypt(base64Value: String): String? {
        return runCatching {
            val encryptedBytes = Base64.decode(base64Value, Base64.NO_WRAP)
            tinkManager.decrypt(encryptedBytes)
        }.getOrNull()
    }

    private companion object {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }
}
