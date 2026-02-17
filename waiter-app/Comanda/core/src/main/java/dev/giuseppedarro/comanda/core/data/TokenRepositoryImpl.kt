package dev.giuseppedarro.comanda.core.data

import android.util.Base64
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.giuseppedarro.comanda.core.domain.TokenRepository
import kotlinx.coroutines.flow.first

class TokenRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
    private val cryptoManager: CryptoManager,
) : TokenRepository {

    override suspend fun saveTokens(access: String, refresh: String) {
        val encAccess = cryptoManager.encrypt(access).encode()
        val encRefresh = cryptoManager.encrypt(refresh).encode()
        dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN] = encAccess
            prefs[REFRESH_TOKEN] = encRefresh
        }
    }

    override suspend fun getAccessToken(): String? {
        val prefs = dataStore.data.first()
        val enc = prefs[ACCESS_TOKEN] ?: return null
        return runCatching { decodeEncrypted(enc) }
            .map { cryptoManager.decrypt(it) }
            .getOrNull()
    }

    override suspend fun getRefreshToken(): String? {
        val prefs = dataStore.data.first()
        val enc = prefs[REFRESH_TOKEN] ?: return null
        return runCatching { decodeEncrypted(enc) }
            .map { cryptoManager.decrypt(it) }
            .getOrNull()
    }

    override suspend fun clear() {
        dataStore.edit { it.clear() }
    }

    override suspend fun saveAccessToken(token: String) {
        val enc = cryptoManager.encrypt(token).encode()
        dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN] = enc
        }
    }

    private fun CryptoManager.EncryptedData.encode(): String {
        val ivB64 = Base64.encodeToString(iv, Base64.NO_WRAP)
        val dataB64 = Base64.encodeToString(data, Base64.NO_WRAP)
        return "$ivB64:$dataB64"
    }

    private fun decodeEncrypted(payload: String): CryptoManager.EncryptedData {
        val parts = payload.split(":")
        val iv = Base64.decode(parts[0], Base64.NO_WRAP)
        val data = Base64.decode(parts[1], Base64.NO_WRAP)
        return CryptoManager.EncryptedData(iv, data)
    }

    private companion object {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }
}
