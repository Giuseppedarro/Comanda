
package dev.giuseppedarro.comanda.core.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import dev.giuseppedarro.comanda.core.domain.model.ThemePreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestName
import org.junit.runner.RunWith
import java.io.File

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ThemeRepositoryImplTest {

    @get:Rule
    val testName = TestName()

    private lateinit var dataStoreFile: File
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var themeRepository: ThemeRepositoryImpl

    private val testContext: Context = ApplicationProvider.getApplicationContext()

    @Before
    fun setUp() {
        dataStoreFile = testContext.filesDir.resolve("${testName.methodName}.preferences_pb")
        dataStore = PreferenceDataStoreFactory.create { dataStoreFile }
        themeRepository = ThemeRepositoryImpl(dataStore)
    }

    @After
    fun tearDown() {
        // Clean up the test file
        dataStoreFile.delete()
    }

    @Test
    fun getThemePreferences_returnsDefaultValues_whenDataStoreIsEmpty() = runTest {
        val result = themeRepository.getThemePreferences().first()

        assertThat(result).isEqualTo(ThemePreferences(useSystemTheme = false, isDarkMode = false))
    }

    @Test
    fun saveThemePreferences_persistsValues() = runTest {
        val preferences = ThemePreferences(useSystemTheme = true, isDarkMode = true)

        themeRepository.saveThemePreferences(preferences)

        val result = themeRepository.getThemePreferences().first()

        assertThat(result).isEqualTo(preferences)
    }

    @Test
    fun saveThemePreferences_updatesExistingValues() = runTest {
        val initial = ThemePreferences(useSystemTheme = false, isDarkMode = false)
        themeRepository.saveThemePreferences(initial)

        val updated = ThemePreferences(useSystemTheme = true, isDarkMode = true)
        themeRepository.saveThemePreferences(updated)

        val result = themeRepository.getThemePreferences().first()

        assertThat(result).isEqualTo(updated)
    }

    @Test
    fun saveThemePreferences_withPartialUpdates() = runTest {
        val initial = ThemePreferences(useSystemTheme = false, isDarkMode = false)
        themeRepository.saveThemePreferences(initial)

        val updated = ThemePreferences(useSystemTheme = true, isDarkMode = false)
        themeRepository.saveThemePreferences(updated)

        val result = themeRepository.getThemePreferences().first()

        assertThat(result).isEqualTo(updated)
        assertThat(result.useSystemTheme).isTrue()
        assertThat(result.isDarkMode).isFalse()
    }

    @Test
    fun getThemePreferences_emitsLatestValue_afterMultipleSaves() = runTest {
        themeRepository.saveThemePreferences(ThemePreferences(useSystemTheme = false, isDarkMode = false))

        themeRepository.saveThemePreferences(ThemePreferences(useSystemTheme = true, isDarkMode = false))

        themeRepository.saveThemePreferences(ThemePreferences(useSystemTheme = true, isDarkMode = true))

        val result = themeRepository.getThemePreferences().first()

        assertThat(result).isEqualTo(ThemePreferences(useSystemTheme = true, isDarkMode = true))
    }
}
