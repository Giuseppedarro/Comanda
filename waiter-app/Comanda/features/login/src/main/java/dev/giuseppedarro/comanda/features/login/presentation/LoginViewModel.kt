package dev.giuseppedarro.comanda.features.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.giuseppedarro.comanda.features.login.domain.use_case.GetBaseUrlUseCase
import dev.giuseppedarro.comanda.features.login.domain.use_case.LoginUseCase
import dev.giuseppedarro.comanda.features.login.domain.use_case.SetBaseUrlUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class LoginUiState(
    val employeeId: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isLoginSuccessful: Boolean = false,
    val errorMessage: String? = null,
    val baseUrl: String = ""
)

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val getBaseUrl: GetBaseUrlUseCase,
    private val setBaseUrl: SetBaseUrlUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    init {
        // Initialize base URL from use case
        _uiState.update { it.copy(baseUrl = getBaseUrl()) }
    }

    fun onEmployeeIdChange(id: String) {
        _uiState.update { it.copy(employeeId = id, errorMessage = null) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, errorMessage = null) }
    }

    fun onBaseUrlChange(text: String) {
        _uiState.update { it.copy(baseUrl = text) }
    }

    fun saveBaseUrl() {
        // Persist via domain use case (which sanitizes)
        setBaseUrl(_uiState.value.baseUrl)
        // Read back normalized value
        _uiState.update { it.copy(baseUrl = getBaseUrl()) }
    }

    fun onLoginClick() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = loginUseCase(
                employeeId = _uiState.value.employeeId,
                password = _uiState.value.password
            )

            result.onSuccess {
                _uiState.update { it.copy(isLoading = false, isLoginSuccessful = true) }
            }.onFailure { exception -> // Give the throwable an explicit name
                _uiState.update { it.copy(isLoading = false, errorMessage = exception.message) }
            }
        }
    }

    fun onLoginHandled() {
        _uiState.update { it.copy(isLoginSuccessful = false) }
    }
}
