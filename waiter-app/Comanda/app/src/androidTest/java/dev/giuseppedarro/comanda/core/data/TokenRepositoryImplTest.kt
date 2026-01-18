package dev.giuseppedarro.comanda.core.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class TokenRepositoryImplTest {

    private lateinit var repository: TokenRepositoryImpl
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var testFile: File

    private val testContext = InstrumentationRegistry.getInstrumentation().targetContext

    private val cryptoManager = CryptoManager(alias = "test_alias")
    private val accessTokenKey = stringPreferencesKey("access_token")

    @Before
    fun setUp() {
        // Create unique file for each test to avoid DataStore conflicts
        testFile = File(
            testContext.cacheDir,
            "test_datastore_${System.currentTimeMillis()}.preferences_pb"
        )

        // Use a real DataStore for instrumented tests
        dataStore = PreferenceDataStoreFactory.create(produceFile = { testFile })
        repository = TokenRepositoryImpl(dataStore, cryptoManager)
    }

    @After
    fun tearDown() {
        // Clean up test file after each test
        if (::testFile.isInitialized && testFile.exists()) {
            testFile.delete()
        }
    }

    @Test
    fun getAccessToken_returnsNull_whenDatastoreIsEmpty() = runTest {
        assertThat(repository.getAccessToken()).isNull()
    }

    @Test
    fun saveAndGetAccessToken_e2e() = runTest {
        val originalToken = "my-secret-access-token"

        repository.saveAccessToken(originalToken)
        val retrievedToken = repository.getAccessToken()

        assertThat(retrievedToken).isEqualTo(originalToken)
    }

    @Test
    fun getAccessToken_returnsNull_whenDecryptionFails() = runTest {
        // Store invalid corrupted data (not from real encryption)
        dataStore.edit { it[accessTokenKey] = "invalid_corrupted_base64_data" }

        // The repo handles errors gracefully and returns null
        assertThat(repository.getAccessToken()).isNull()
    }

    @Test
    fun clear_removesAllData() = runTest {
        // Given - save some data
        repository.saveAccessToken("some-token")

        // When
        repository.clear()

        // Then
        val prefs = dataStore.data.first()
        assertThat(prefs.asMap()).isEmpty()
        assertThat(repository.getAccessToken()).isNull()
    }
}