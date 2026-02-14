package dev.giuseppedarro.comanda.features.settings.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.giuseppedarro.comanda.R
import dev.giuseppedarro.comanda.core.presentation.ComandaTopAppBar
import dev.giuseppedarro.comanda.features.settings.domain.model.User
import dev.giuseppedarro.comanda.ui.theme.ComandaTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun ManageUsersScreen(
    onBackClick: () -> Unit,
    viewModel: ManageUsersViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.eventChannel.collect {
            when (it) {
                is ManageUsersViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(it.message)
                }
            }
        }
    }

    ManageUsersContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onAddUserClick = viewModel::onAddUserClick,
        onEditUserClick = viewModel::onEditUserClick,
        onDeleteUserClick = viewModel::onDeleteUserClick,
        onRefresh = viewModel::onRefresh,
        snackbarHostState = snackbarHostState
    )

    if (uiState.showAddUserDialog) {
        AddUserDialog(
            isLoading = uiState.isLoading,
            state = uiState.addUserDialogState,
            onStateChange = viewModel::onAddUserDialogStateChange,
            onDismissRequest = viewModel::onDismissAddUserDialog,
            onConfirm = viewModel::createUser
        )
    }

    if (uiState.showEditUserDialog) {
        EditUserDialog(
            isLoading = uiState.isLoading,
            state = uiState.editUserDialogState,
            onStateChange = viewModel::onEditUserDialogStateChange,
            onDismissRequest = viewModel::onDismissEditUserDialog,
            onConfirm = viewModel::updateUser
        )
    }

    if (uiState.showDeleteUserDialog) {
        DeleteUserDialog(
            isLoading = uiState.isLoading,
            onDismissRequest = viewModel::onDismissDeleteUserDialog,
            onConfirm = viewModel::deleteUser
        )
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
private fun ManageUsersContent(
    uiState: ManageUsersState,
    onBackClick: () -> Unit,
    onAddUserClick: () -> Unit,
    onEditUserClick: (User) -> Unit,
    onDeleteUserClick: (User) -> Unit,
    onRefresh: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val pullRefreshState = rememberPullRefreshState(uiState.isRefreshing || uiState.isLoading, onRefresh)

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            ComandaTopAppBar(
                title = stringResource(R.string.manage_users),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddUserClick) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_user))
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .pullRefresh(pullRefreshState)
        ) {
            when {
                uiState.users.isEmpty() && !uiState.isLoading && !uiState.isRefreshing -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.no_users_found),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.add_user_prompt),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp)
                    ) {
                        items(uiState.users, key = { it.id }) { user ->
                            UserItem(
                                modifier = Modifier.animateItem(),
                                user = user,
                                onEdit = { onEditUserClick(user) },
                                onDelete = { onDeleteUserClick(user) })
                        }
                    }
                }
            }

            PullRefreshIndicator(
                refreshing = uiState.isRefreshing || uiState.isLoading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
private fun UserItem(
    user: User,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = user.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = user.role,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onEdit) {
                Icon(
                    imageVector = Icons.Default.Edit, contentDescription = stringResource(R.string.edit_user),
                    tint = MaterialTheme.colorScheme.primary
                    )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete, contentDescription = stringResource(R.string.delete_user),
                    tint = MaterialTheme.colorScheme.primary
                    )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddUserDialog(
    isLoading: Boolean,
    state: AddUserDialogState,
    onStateChange: (AddUserDialogState) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(stringResource(R.string.add_new_user)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    OutlinedTextField(
                        value = state.employeeId,
                        onValueChange = { onStateChange(state.copy(employeeId = it)) },
                        label = { Text(stringResource(R.string.employee_id)) },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = state.name,
                        onValueChange = { onStateChange(state.copy(name = it)) },
                        label = { Text(stringResource(R.string.name)) },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = state.password,
                        onValueChange = { onStateChange(state.copy(password = it)) },
                        label = { Text(stringResource(R.string.password)) },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = state.role == "WAITER",
                            onClick = { onStateChange(state.copy(role = "WAITER")) },
                            label = { Text(stringResource(R.string.waiter)) }
                        )
                        FilterChip(
                            selected = state.role == "ADMIN",
                            onClick = { onStateChange(state.copy(role = "ADMIN")) },
                            label = { Text(stringResource(R.string.admin)) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                enabled = !isLoading
            ) {
                Text(stringResource(R.string.create))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest, enabled = !isLoading) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditUserDialog(
    isLoading: Boolean,
    state: EditUserDialogState,
    onStateChange: (EditUserDialogState) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(stringResource(R.string.edit_user)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    OutlinedTextField(
                        value = state.employeeId,
                        onValueChange = { onStateChange(state.copy(employeeId = it)) },
                        label = { Text(stringResource(R.string.employee_id)) },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = state.name,
                        onValueChange = { onStateChange(state.copy(name = it)) },
                        label = { Text(stringResource(R.string.name)) },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = state.password,
                        onValueChange = { onStateChange(state.copy(password = it)) },
                        label = { Text(stringResource(R.string.new_password)) },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = state.role == "WAITER",
                            onClick = { onStateChange(state.copy(role = "WAITER")) },
                            label = { Text(stringResource(R.string.waiter)) }
                        )
                        FilterChip(
                            selected = state.role == "ADMIN",
                            onClick = { onStateChange(state.copy(role = "ADMIN")) },
                            label = { Text(stringResource(R.string.admin)) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                enabled = !isLoading
            ) {
                Text(stringResource(R.string.update))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest, enabled = !isLoading) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@Composable
private fun DeleteUserDialog(
    isLoading: Boolean,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(stringResource(R.string.delete_user)) },
        text = { Text(stringResource(R.string.delete_user_confirmation)) },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                enabled = !isLoading
            ) {
                Text(stringResource(R.string.delete))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest, enabled = !isLoading) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}


@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
private fun ManageUsersContentPreview() {
    ComandaTheme {
        val mockUsers = listOf(
            User("1", "001", "Giuseppe D'Arrò", "ADMIN"),
            User("2", "002", "Mario Rossi", "WAITER"),
            User("3", "003", "Jan Smit", "WAITER")
        )

        ManageUsersContent(
            uiState = ManageUsersState(users = mockUsers),
            onBackClick = {},
            onAddUserClick = {},
            onEditUserClick = {},
            onDeleteUserClick = {},
            onRefresh = {},
            snackbarHostState = SnackbarHostState()
        )
    }
}
