package dev.giuseppedarro.comanda.core.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class TokenRepositoryImplTest {

    private lateinit var repository: TokenRepositoryImpl
    private lateinit var dataStore: DataStore<Preferences>
    private val cryptoManager: CryptoManager = mockk()

    private val testContext = InstrumentationRegistry.getInstrumentation().targetContext
    private val testFile = File(testContext.cacheDir, "test_datastore.preferences_pb")

    private val accessTokenKey = stringPreferencesKey("access_token")

    @Before
    fun setUp() {
        // Use a real DataStore for instrumented tests
        dataStore = PreferenceDataStoreFactory.create(produceFile = { testFile })
        // Spy allows us to verify calls on a real object
        dataStore = spyk(dataStore)

        repository = TokenRepositoryImpl(dataStore, cryptoManager)
    }

    @Test
    fun getAccessToken_returnsNull_whenDatastoreIsEmpty() = runTest {
        // Given an empty datastore
        // When
        val token = repository.getAccessToken()
        // Then
        assertThat(token).isNull()
    }

    @Test
    fun saveAndGetAccessToken_e2e() = runTest {
        // This is a more realistic integration test
        val realCryptoManager = CryptoManager(alias = "test_alias_e2e")
        val realRepository = TokenRepositoryImpl(dataStore, realCryptoManager)

        val originalToken = "my-secret-access-token"

        // When
        realRepository.saveAccessToken(originalToken)
        val retrievedToken = realRepository.getAccessToken()

        // Then
        assertThat(retrievedToken).isEqualTo(originalToken)
    }
	
    @Test
    fun getAccessToken_decryptsAndReturnsToken_correctly() = runTest {
        // Given
        val decryptedToken = "decrypted-access-token"
        val encryptedData = CryptoManager.EncryptedData("iv".toByteArray(), "data".toByteArray())
        val encodedString = "aXY=:ZGF0YQ==" // Base64 encoding of "iv" and "data"

        dataStore.edit { it[accessTokenKey] = encodedString }
        every { cryptoManager.decrypt(any()) } returns decryptedToken

        // When
        val token = repository.getAccessToken()

        // Then
        assertThat(token).isEqualTo(decryptedToken)
        coVerify { cryptoManager.decrypt(encryptedData) }
    }

    @Test
    fun getAccessToken_returnsNull_onDecryptionError() = runTest {
        // Given
        val encodedString = "aXY=:ZGF0YQ==" // Corrupted or invalid string
        dataStore.edit { it[accessTokenKey] = encodedString }
        every { cryptoManager.decrypt(any()) } throws RuntimeException("Decryption failed")

        // When
        val token = repository.getAccessToken()

        // Then
        assertThat(token).isNull()
    }

    @Test
    fun clear_callsDatastoreClear() = runTest {
        // Given - some data
        repository.saveAccessToken("some-token")

        // When
        repository.clear()

        // Then
        val prefs = dataStore.data.first()
        assertThat(prefs.asMap()).isEmpty()
        // Also verify the interaction if needed (using spyk)
        coVerify { dataStore.edit(any()) }
    }
}