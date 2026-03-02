package dev.giuseppedarro.comanda.features.settings.presentation

import androidx.lifecycle.ViewModel
import dev.giuseppedarro.comanda.core.domain.usecase.GetLanguageUseCase
import dev.giuseppedarro.comanda.core.domain.usecase.SetLanguageUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LanguageSettingsViewModel(
    private val getLanguageUseCase: GetLanguageUseCase,
    private val setLanguageUseCase: SetLanguageUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LanguageSettingsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        val currentLanguage = getLanguageUseCase()
        _uiState.update {
            it.copy(
                currentLanguage = currentLanguage,
                useSystemLanguage = currentLanguage.isEmpty()
            )
        }
    }

    fun onLanguageChange(language: String) {
        setLanguageUseCase(language)
        _uiState.update { it.copy(currentLanguage = language) }
    }

    fun onUseSystemLanguageChange(useSystem: Boolean) {
        if (useSystem) {
            setLanguageUseCase("")
            _uiState.update { it.copy(useSystemLanguage = true, currentLanguage = "") }
        } else {
            val defaultLanguage = "en"
            setLanguageUseCase(defaultLanguage)
            _uiState.update { it.copy(useSystemLanguage = false, currentLanguage = defaultLanguage) }
        }
    }
}

data class LanguageSettingsUiState(
    val currentLanguage: String = "",
    val useSystemLanguage: Boolean = false
)
