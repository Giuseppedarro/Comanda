package dev.giuseppedarro.comanda.features.login.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class LoginUiState(
    val employeeId: String = "",
    val password: String = ""
    // We can add loading and error states here later
)

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmployeeIdChange(id: String) {
        _uiState.update { it.copy(employeeId = id) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    // The onLoginClick logic will live here.
    // For now, it won't do anything, as it will later call a UseCase.
    fun onLoginClick(onLoginSuccess: () -> Unit) {
        // TODO: Implement real login logic by calling a use case
        // For now, we'll just trigger the navigation callback.
        onLoginSuccess()
    }
}
