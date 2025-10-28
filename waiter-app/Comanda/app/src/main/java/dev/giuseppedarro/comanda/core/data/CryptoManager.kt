package dev.giuseppedarro.comanda.core.data

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * Minimal AES/GCM encryption helper backed by Android Keystore.
 * Stores a symmetric key under [alias] in the AndroidKeyStore.
 */
class CryptoManager(
    private val alias: String = DEFAULT_ALIAS,
) {
    private val keyStore: KeyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }

    private fun getOrCreateKey(): SecretKey {
        // Return if it already exists
        (keyStore.getEntry(alias, null) as? KeyStore.SecretKeyEntry)?.let { return it.secretKey }

        // Otherwise create a new AES/GCM key
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
        val parameterSpec = KeyGenParameterSpec.Builder(
            alias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setRandomizedEncryptionRequired(true)
            .build()
        keyGenerator.init(parameterSpec)
        return keyGenerator.generateKey()
    }

    fun encrypt(plainText: String): EncryptedData {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getOrCreateKey())
        val iv = cipher.iv
        val cipherBytes = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))
        return EncryptedData(iv = iv, data = cipherBytes)
    }

    fun decrypt(encrypted: EncryptedData): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val spec = GCMParameterSpec(GCM_TAG_LENGTH_BITS, encrypted.iv)
        cipher.init(Cipher.DECRYPT_MODE, getOrCreateKey(), spec)
        val plainBytes = cipher.doFinal(encrypted.data)
        return String(plainBytes, Charsets.UTF_8)
    }

    data class EncryptedData(val iv: ByteArray, val data: ByteArray)

    companion object {
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private const val GCM_TAG_LENGTH_BITS = 128
        const val DEFAULT_ALIAS = "comanda_tokens_key"
    }
}
