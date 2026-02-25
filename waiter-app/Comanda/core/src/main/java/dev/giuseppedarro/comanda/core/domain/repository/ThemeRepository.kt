
package dev.giuseppedarro.comanda.core.domain.repository

import dev.giuseppedarro.comanda.core.domain.model.ThemePreferences
import kotlinx.coroutines.flow.Flow

interface ThemeRepository {

    fun getThemePreferences(): Flow<ThemePreferences>

    suspend fun saveThemePreferences(themePreferences: ThemePreferences)
}
