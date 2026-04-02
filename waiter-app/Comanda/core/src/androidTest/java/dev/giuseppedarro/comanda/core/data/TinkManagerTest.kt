package dev.giuseppedarro.comanda.core.data

import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class TinkManagerTest {

    private lateinit var tinkManager: TinkManager

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        tinkManager = TinkManager(context)
    }

    @Test
    fun encryptAndDecrypt_returnsOriginalString() {
        val originalText = "super-secret-token-123"
        
        val encrypted = tinkManager.encrypt(originalText)
        val decrypted = tinkManager.decrypt(encrypted)

        assertThat(decrypted).isEqualTo(originalText)
        assertThat(encrypted).isNotEqualTo(originalText.toByteArray())
    }

    @Test
    fun encrypt_multipleTimes_returnsDifferentCiphertext() {
        val text = "consistent-text"
        
        val encrypted1 = tinkManager.encrypt(text)
        val encrypted2 = tinkManager.encrypt(text)

        // AES-GCM should produce different ciphertext for each call due to random IVs
        assertThat(encrypted1).isNotEqualTo(encrypted2)
        
        // Both should decrypt to the same text
        assertThat(tinkManager.decrypt(encrypted1)).isEqualTo(text)
        assertThat(tinkManager.decrypt(encrypted2)).isEqualTo(text)
    }

    @Test
    fun decrypt_withInvalidData_returnsNull() {
        val invalidData = "not-encrypted-at-all".toByteArray()
        
        val result = tinkManager.decrypt(invalidData)

        assertThat(result).isNull()
    }
}
