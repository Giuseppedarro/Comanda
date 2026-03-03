
package dev.giuseppedarro.comanda.features.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.giuseppedarro.comanda.core.domain.model.ThemePreferences
import dev.giuseppedarro.comanda.core.domain.usecase.GetThemePreferencesUseCase
import dev.giuseppedarro.comanda.core.domain.usecase.SaveThemePreferencesUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ThemeSettingsUiState(
    val useSystemTheme: Boolean = false,
    val isDarkMode: Boolean = false
)

class ThemeSettingsViewModel(
    private val getThemePreferencesUseCase: GetThemePreferencesUseCase,
    private val saveThemePreferencesUseCase: SaveThemePreferencesUseCase
) : ViewModel() {

    val uiState: StateFlow<ThemeSettingsUiState> = getThemePreferencesUseCase()
        .map { ThemeSettingsUiState(it.useSystemTheme, it.isDarkMode) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ThemeSettingsUiState()
        )

    fun onUseSystemThemeChange(useSystemTheme: Boolean) {
        viewModelScope.launch {
            saveThemePreferencesUseCase(ThemePreferences(useSystemTheme, uiState.value.isDarkMode))
        }
    }

    fun onDarkModeChange(isDarkMode: Boolean) {
        viewModelScope.launch {
            saveThemePreferencesUseCase(ThemePreferences(uiState.value.useSystemTheme, isDarkMode))
        }
    }
}
