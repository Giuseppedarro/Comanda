package dev.giuseppedarro.comanda.features.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.giuseppedarro.comanda.features.settings.data.remote.dto.CreateUserRequest
import dev.giuseppedarro.comanda.features.settings.domain.use_case.CreateUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ManageUsersUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isUserCreated: Boolean = false
)

class ManageUsersViewModel(private val createUserUseCase: CreateUserUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow(ManageUsersUiState())
    val uiState = _uiState.asStateFlow()

    fun createUser(createUserRequest: CreateUserRequest) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            createUserUseCase(createUserRequest)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false, isUserCreated = true) }
                }
                .onFailure { exception ->
                    _uiState.update { it.copy(isLoading = false, error = exception.message) }
                }
        }
    }

    fun onUserCreatedConsumed() {
        _uiState.update { it.copy(isUserCreated = false) }
    }

    fun onErrorConsumed() {
        _uiState.update { it.copy(error = null) }
    }
}
