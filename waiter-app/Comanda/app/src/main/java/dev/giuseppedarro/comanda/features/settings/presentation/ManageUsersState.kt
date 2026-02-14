package dev.giuseppedarro.comanda.features.settings.presentation

import dev.giuseppedarro.comanda.features.settings.domain.model.User

data class ManageUsersState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val users: List<User> = emptyList(),
    val error: String? = null,
    val isUserCreated: Boolean = false,
    val showAddUserDialog: Boolean = false,
    val showEditUserDialog: Boolean = false,
    val showDeleteUserDialog: Boolean = false,
    val selectedUser: User? = null,
    val addUserDialogState: AddUserDialogState = AddUserDialogState(),
    val editUserDialogState: EditUserDialogState = EditUserDialogState()
)

data class AddUserDialogState(
    val employeeId: String = "",
    val name: String = "",
    val password: String = "",
    val role: String = "WAITER",
)

data class EditUserDialogState(
    val name: String = "",
    val password: String = "",
    val role: String = "",
    val employeeId: String = ""
)
