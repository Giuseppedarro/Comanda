package dev.giuseppedarro.comanda.core.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.security.KeyStore

@RunWith(AndroidJUnit4::class)
class CryptoManagerTest {

    private lateinit var cryptoManager: CryptoManager

    private val testAlias = "test_key_alias"

    @Before
    fun setUp() {
        // Ensure the keystore is clean before each test
        val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        if (keyStore.containsAlias(testAlias)) {
            keyStore.deleteEntry(testAlias)
        }

        cryptoManager = CryptoManager(alias = testAlias)
    }

    @Test
    fun encryptAndDecrypt_returnsOriginalPlaintext() {
        // Given
        val plaintext = "This is a secret message for testing!"

        // When
        val encryptedData = cryptoManager.encrypt(plaintext)
        val decryptedText = cryptoManager.decrypt(encryptedData)

        // Then
        assertThat(decryptedText).isEqualTo(plaintext)
    }

    @Test
    fun encryption_producesDifferentCiphertextForSamePlaintext() {
        // Given
        val plaintext = "This should be encrypted differently each time."

        // When
        val encryptedData1 = cryptoManager.encrypt(plaintext)
        val encryptedData2 = cryptoManager.encrypt(plaintext)

        // Then
        // the encrypted data should be different.
        assertThat(encryptedData1.data).isNotEqualTo(encryptedData2.data)
        assertThat(encryptedData1.iv).isNotEqualTo(encryptedData2.iv)

        // But they should both decrypt to the same original text
        val decryptedText1 = cryptoManager.decrypt(encryptedData1)
        val decryptedText2 = cryptoManager.decrypt(encryptedData2)
        assertThat(decryptedText1).isEqualTo(plaintext)
        assertThat(decryptedText2).isEqualTo(plaintext)
    }

    @Test
    fun handlesEmptyStringCorrectly() {
        // Given
        val plaintext = ""

        // When
        val encryptedData = cryptoManager.encrypt(plaintext)
        val decryptedText = cryptoManager.decrypt(encryptedData)

        // Then
        assertThat(decryptedText).isEqualTo(plaintext)
    }
}