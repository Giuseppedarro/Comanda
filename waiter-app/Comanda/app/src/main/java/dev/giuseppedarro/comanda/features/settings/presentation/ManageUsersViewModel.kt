package dev.giuseppedarro.comanda.features.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.giuseppedarro.comanda.features.settings.data.remote.dto.CreateUserRequest
import dev.giuseppedarro.comanda.features.settings.data.remote.dto.UpdateUserRequest
import dev.giuseppedarro.comanda.features.settings.domain.model.User
import dev.giuseppedarro.comanda.features.settings.domain.use_case.CreateUserUseCase
import dev.giuseppedarro.comanda.features.settings.domain.use_case.DeleteUserUseCase
import dev.giuseppedarro.comanda.features.settings.domain.use_case.GetUsersUseCase
import dev.giuseppedarro.comanda.features.settings.domain.use_case.UpdateUserUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ManageUsersViewModel(
    private val createUserUseCase: CreateUserUseCase,
    private val getUsersUseCase: GetUsersUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ManageUsersState())
    val uiState = _uiState.asStateFlow()

    private val _eventChannel = Channel<UiEvent>()
    val eventChannel = _eventChannel.receiveAsFlow()

    init {
        getUsers()
    }

    fun onRefresh() {
        getUsers(isRefreshing = true)
    }

    private fun getUsers(isRefreshing: Boolean = false) {
        getUsersUseCase()
            .onStart {
                if (isRefreshing) {
                    _uiState.update { it.copy(isRefreshing = true) }
                } else {
                    _uiState.update { it.copy(isLoading = true) }
                }
            }
            .onEach { result ->
                result
                    .onSuccess { users ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isRefreshing = false,
                                users = users
                            )
                        }
                    }
                    .onFailure { exception ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isRefreshing = false,
                                error = exception.message
                            )
                        }
                        _eventChannel.send(UiEvent.ShowSnackbar("Error: ${exception.message}"))
                    }
            }
            .catch { exception ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isRefreshing = false,
                        error = exception.message
                    )
                }
                _eventChannel.send(UiEvent.ShowSnackbar("Error: ${exception.message}"))
            }
            .launchIn(viewModelScope)
    }

    fun onAddUserClick() {
        _uiState.update { it.copy(showAddUserDialog = true) }
    }

    fun onDismissAddUserDialog() {
        _uiState.update { it.copy(showAddUserDialog = false) }
    }

    fun onAddUserDialogStateChange(state: AddUserDialogState) {
        _uiState.update { it.copy(addUserDialogState = state) }
    }

    fun onEditUserClick(user: User) {
        _uiState.update { it.copy(showEditUserDialog = true, selectedUser = user) }
    }

    fun onDismissEditUserDialog() {
        _uiState.update { it.copy(showEditUserDialog = false, selectedUser = null) }
    }

    fun onEditUserDialogStateChange(state: EditUserDialogState) {
        _uiState.update { it.copy(editUserDialogState = state) }
    }

    fun onDeleteUserClick(user: User) {
        _uiState.update { it.copy(showDeleteUserDialog = true, selectedUser = user) }
    }

    fun onDismissDeleteUserDialog() {
        _uiState.update { it.copy(showDeleteUserDialog = false, selectedUser = null) }
    }

    fun createUser() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val dialogState = _uiState.value.addUserDialogState
            val request = CreateUserRequest(
                employeeId = dialogState.employeeId,
                name = dialogState.name,
                password = dialogState.password,
                role = dialogState.role.lowercase()
            )

            createUserUseCase(request)
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isUserCreated = true,
                            showAddUserDialog = false
                        )
                    }
                    _eventChannel.send(UiEvent.ShowSnackbar("User created successfully!"))
                    getUsers() // Refresh the user list
                }
                .onFailure { exception ->
                    _uiState.update { it.copy(isLoading = false, error = exception.message) }
                    _eventChannel.send(UiEvent.ShowSnackbar("Error: ${exception.message}"))
                }
        }
    }

    fun updateUser() {
        viewModelScope.launch {
            _uiState.value.selectedUser?.let { user ->
                _uiState.update { it.copy(isLoading = true) }

                val dialogState = _uiState.value.editUserDialogState
                val request = UpdateUserRequest(
                    name = dialogState.name.takeIf { it.isNotBlank() },
                    password = dialogState.password.takeIf { it.isNotBlank() },
                    role = dialogState.role.takeIf { it.isNotBlank() },
                    employeeId = dialogState.employeeId.takeIf { it.isNotBlank() }
                )

                updateUserUseCase(user.id, request)
                    .onSuccess {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                showEditUserDialog = false,
                                selectedUser = null
                            )
                        }
                        _eventChannel.send(UiEvent.ShowSnackbar("User updated successfully!"))
                        getUsers() // Refresh the user list
                    }
                    .onFailure { exception ->
                        _uiState.update { it.copy(isLoading = false, error = exception.message) }
                        _eventChannel.send(UiEvent.ShowSnackbar("Error: ${exception.message}"))
                    }
            }
        }
    }

    fun deleteUser() {
        viewModelScope.launch {
            _uiState.value.selectedUser?.let { user ->
                _uiState.update { it.copy(isLoading = true) }

                deleteUserUseCase(user.id)
                    .onSuccess {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                showDeleteUserDialog = false,
                                selectedUser = null
                            )
                        }
                        _eventChannel.send(UiEvent.ShowSnackbar("User deleted successfully!"))
                        getUsers() // Refresh the user list
                    }
                    .onFailure { exception ->
                        _uiState.update { it.copy(isLoading = false, error = exception.message) }
                        _eventChannel.send(UiEvent.ShowSnackbar("Error: ${exception.message}"))
                    }
            }
        }
    }

    fun onUserCreatedConsumed() {
        _uiState.update { it.copy(isUserCreated = false) }
    }

    fun onErrorConsumed() {
        _uiState.update { it.copy(error = null) }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
    }
}
