package dev.giuseppedarro.comanda.features.settings.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.giuseppedarro.comanda.R
import dev.giuseppedarro.comanda.core.presentation.ComandaTopAppBar
import dev.giuseppedarro.comanda.features.settings.data.remote.dto.CreateUserRequest
import dev.giuseppedarro.comanda.ui.theme.ComandaTheme
import org.koin.androidx.compose.koinViewModel

// Mock data until the GET endpoint is implemented
data class MockUser(val id: String, val name: String, val role: String)

private val mockUsers = listOf(
    MockUser("1", "Giuseppe D'Arrò", "ADMIN"),
    MockUser("2", "John Doe", "WAITER"),
    MockUser("3", "Jane Smith", "WAITER")
)

@Composable
fun ManageUsersScreen(
    onBackClick: () -> Unit,
    viewModel: ManageUsersViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddUserDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState) {
        if (uiState.isUserCreated) {
            showAddUserDialog = false
            snackbarHostState.showSnackbar("User created successfully!")
            viewModel.onUserCreatedConsumed()
        }
        if (uiState.error != null) {
            snackbarHostState.showSnackbar("Error: ${uiState.error}")
            viewModel.onErrorConsumed()
        }
    }

    ManageUsersContent(
        users = mockUsers,
        onBackClick = onBackClick,
        onAddUserClick = { showAddUserDialog = true },
        onEditUserClick = { /* TODO */ },
        onDeleteUserClick = { /* TODO */ },
        snackbarHostState = snackbarHostState
    )

    if (showAddUserDialog) {
        AddUserDialog(
            isLoading = uiState.isLoading,
            onDismissRequest = { showAddUserDialog = false },
            onConfirm = viewModel::createUser
        )
    }
}

@Composable
private fun ManageUsersContent(
    users: List<MockUser>,
    onBackClick: () -> Unit,
    onAddUserClick: () -> Unit,
    onEditUserClick: (MockUser) -> Unit,
    onDeleteUserClick: (MockUser) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            ComandaTopAppBar(
                title = "Manage Users",
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
                Icon(Icons.Default.Add, contentDescription = "Add User")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(users, key = { it.id }) {
                UserItem(user = it, onEdit = { onEditUserClick(it) }, onDelete = { onDeleteUserClick(it) })
            }
        }
    }
}

@Composable
private fun UserItem(
    user: MockUser,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = user.name, style = MaterialTheme.typography.titleMedium)
                Text(text = user.role, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Edit User")
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete User")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddUserDialog(
    isLoading: Boolean,
    onDismissRequest: () -> Unit,
    onConfirm: (CreateUserRequest) -> Unit
) {
    var employeeId by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("WAITER") }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Add New User") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    OutlinedTextField(
                        value = employeeId,
                        onValueChange = { employeeId = it },
                        label = { Text("Employee ID") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Name") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = role == "WAITER",
                            onClick = { role = "WAITER" },
                            label = { Text("Waiter") }
                        )
                        FilterChip(
                            selected = role == "ADMIN",
                            onClick = { role = "ADMIN" },
                            label = { Text("Admin") }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val request = CreateUserRequest(employeeId, name, password, role.lowercase())
                    onConfirm(request)
                },
                enabled = !isLoading
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest, enabled = !isLoading) {
                Text("Cancel")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun ManageUsersContentPreview() {
    ComandaTheme {
        ManageUsersContent(
            users = mockUsers,
            onBackClick = {},
            onAddUserClick = {},
            onEditUserClick = {},
            onDeleteUserClick = {},
            snackbarHostState = SnackbarHostState()
        )
    }
}
