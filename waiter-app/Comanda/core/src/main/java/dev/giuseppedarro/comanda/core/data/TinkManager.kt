package dev.giuseppedarro.comanda.core.data

import android.content.Context
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager

/**
 * Modern encryption helper using Google Tink.
 * Handles Keystore integration and AEAD (Authenticated Encryption with Associated Data)
 * using AES-GCM.
 */
class TinkManager(context: Context) {

    private val aead: Aead by lazy {
        AeadConfig.register()
        AndroidKeysetManager.Builder()
            .withSharedPref(context, KEYSET_NAME, PREF_FILE_NAME)
            .withKeyTemplate(KeyTemplates.get("AES256_GCM"))
            .withMasterKeyUri(MASTER_KEY_URI)
            .build()
            .keysetHandle
            .getPrimitive(Aead::class.java)
    }

    fun encrypt(plainText: String): ByteArray {
        return aead.encrypt(plainText.toByteArray(Charsets.UTF_8), null)
    }

    fun decrypt(encryptedData: ByteArray): String? {
        return runCatching {
            val decryptedBytes = aead.decrypt(encryptedData, null)
            String(decryptedBytes, Charsets.UTF_8)
        }.getOrNull()
    }

    companion object {
        private const val KEYSET_NAME = "comanda_token_keyset"
        private const val PREF_FILE_NAME = "comanda_tink_prefs"
        private const val MASTER_KEY_URI = "android-keystore://comanda_token_master_key"
    }
}
