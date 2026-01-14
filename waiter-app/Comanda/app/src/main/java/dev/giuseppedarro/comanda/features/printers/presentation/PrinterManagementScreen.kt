package dev.giuseppedarro.comanda.features.printers.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.giuseppedarro.comanda.core.presentation.ComandaTopAppBar
import dev.giuseppedarro.comanda.features.printers.domain.model.Printer
import dev.giuseppedarro.comanda.features.printers.presentation.components.AddPrinterDialog
import dev.giuseppedarro.comanda.features.printers.presentation.components.EditPrinterDialog
import dev.giuseppedarro.comanda.features.printers.presentation.components.PrinterListItem
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrinterManagementScreen(
    modifier: Modifier = Modifier,
    viewModel: PrinterManagementViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Add Printer Dialog
    if (uiState.isAddDialogOpen) {
        AddPrinterDialog(
            onDismiss = { viewModel.closeAddDialog() },
            onConfirm = { name, address, port ->
                viewModel.createPrinter(name, address, port)
            }
        )
    }

    // Edit Printer Dialog
    if (uiState.isEditDialogOpen && uiState.editingPrinter != null) {
        EditPrinterDialog(
            printer = uiState.editingPrinter!!,
            onDismiss = { viewModel.closeEditDialog() },
            onConfirm = { id, name, address, port ->
                viewModel.updatePrinter(id, name, address, port)
            }
        )
    }

    PrinterManagementContent(
        printers = uiState.printers,
        isLoading = uiState.isLoading,
        error = uiState.error,
        onRefresh = viewModel::loadPrinters,
        onAddClick = viewModel::openAddDialog,
        onEditClick = viewModel::openEditDialog,
        onDeleteClick = viewModel::deletePrinter,
        onClearError = viewModel::clearError,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrinterManagementContent(
    printers: List<Printer>,
    isLoading: Boolean,
    error: String?,
    onRefresh: () -> Unit,
    onAddClick: () -> Unit,
    onEditClick: (Printer) -> Unit,
    onDeleteClick: (Printer) -> Unit,
    onClearError: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            ComandaTopAppBar(
                title = "Printer Management",
                actions = {
                    IconButton(onClick = onRefresh) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Printer"
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                printers.isEmpty() && !isLoading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No printers configured",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tap the + button to add a new printer",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(
                            items = printers,
                            key = { it.id }
                        ) { printer ->
                            PrinterListItem(
                                printer = printer,
                                onEditClick = { onEditClick(it) },
                                onDeleteClick = { onDeleteClick(it) }
                            )
                        }
                    }
                }
            }

            // Error Snackbar
            error?.let { errorMessage ->
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    action = {
                        TextButton(onClick = onClearError) {
                            Text("Dismiss")
                        }
                    }
                ) {
                    Text(errorMessage)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PrinterManagementScreenPreview() {
    val mockPrinters = listOf(
        Printer(
            id = 1,
            name = "Kitchen Printer",
            address = "192.168.1.100",
            port = 9100
        ),
        Printer(
            id = 2,
            name = "Bar Printer",
            address = "192.168.1.101",
            port = 9100
        ),
        Printer(
            id = 3,
            name = "Dessert Printer",
            address = "192.168.1.102",
            port = 9100
        )
    )

    dev.giuseppedarro.comanda.ui.theme.ComandaTheme {
        PrinterManagementContent(
            printers = mockPrinters,
            isLoading = false,
            error = null,
            onRefresh = {},
            onAddClick = {},
            onEditClick = {},
            onDeleteClick = {},
            onClearError = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PrinterManagementScreenEmptyPreview() {
    dev.giuseppedarro.comanda.ui.theme.ComandaTheme {
        PrinterManagementContent(
            printers = emptyList(),
            isLoading = false,
            error = null,
            onRefresh = {},
            onAddClick = {},
            onEditClick = {},
            onDeleteClick = {},
            onClearError = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PrinterManagementScreenLoadingPreview() {
    dev.giuseppedarro.comanda.ui.theme.ComandaTheme {
        PrinterManagementContent(
            printers = emptyList(),
            isLoading = true,
            error = null,
            onRefresh = {},
            onAddClick = {},
            onEditClick = {},
            onDeleteClick = {},
            onClearError = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PrinterManagementScreenErrorPreview() {
    dev.giuseppedarro.comanda.ui.theme.ComandaTheme {
        PrinterManagementContent(
            printers = emptyList(),
            isLoading = false,
            error = "Failed to load printers. Please check your connection.",
            onRefresh = {},
            onAddClick = {},
            onEditClick = {},
            onDeleteClick = {},
            onClearError = {}
        )
    }
}