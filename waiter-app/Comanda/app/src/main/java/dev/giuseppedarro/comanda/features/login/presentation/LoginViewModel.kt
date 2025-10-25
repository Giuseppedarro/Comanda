package dev.giuseppedarro.comanda.features.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
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
    val errorMessage: String? = null
)

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmployeeIdChange(id: String) {
        _uiState.update { it.copy(employeeId = id, errorMessage = null) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, errorMessage = null) }
    }

    fun onLoginClick() {
        viewModelScope.launch {
            // Start loading
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            // Simulate network delay
            delay(2000)

            // Simulate success or failure
            if (_uiState.value.employeeId == "1234" && _uiState.value.password == "password") {
                // Success
                _uiState.update { it.copy(isLoading = false, isLoginSuccessful = true) }
            } else {
                // Failure
                _uiState.update { it.copy(isLoading = false, errorMessage = "Invalid credentials. Please try again.") }
            }
        }
    }

    fun onLoginHandled() {
        _uiState.update { it.copy(isLoginSuccessful = false) }
    }
}
