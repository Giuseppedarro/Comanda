
package dev.giuseppedarro.comanda.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import dev.giuseppedarro.comanda.core.domain.model.ThemePreferences
import dev.giuseppedarro.comanda.core.domain.repository.ThemeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class ThemeRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : ThemeRepository {

    private object PreferencesKeys {
        val USE_SYSTEM_THEME = booleanPreferencesKey("use_system_theme")
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
    }

    override fun getThemePreferences(): Flow<ThemePreferences> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val useSystemTheme = preferences[PreferencesKeys.USE_SYSTEM_THEME] ?: false
                val isDarkMode = preferences[PreferencesKeys.IS_DARK_MODE] ?: false
                ThemePreferences(useSystemTheme, isDarkMode)
            }
    }

    override suspend fun saveThemePreferences(themePreferences: ThemePreferences) {
        dataStore.edit {
            it[PreferencesKeys.USE_SYSTEM_THEME] = themePreferences.useSystemTheme
            it[PreferencesKeys.IS_DARK_MODE] = themePreferences.isDarkMode
        }
    }
}
