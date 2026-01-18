package dev.giuseppedarro.comanda.features.menu.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.giuseppedarro.comanda.core.presentation.ComandaTopAppBar
import dev.giuseppedarro.comanda.ui.theme.ComandaTheme
import dev.giuseppedarro.comanda.features.menu.domain.model.MenuItem
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CategoryViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    CategoryScreenContent(
        categoryName = uiState.categoryName,
        items = uiState.items,
        isLoading = uiState.isLoading,
        isDialogShown = uiState.isDialogShown,
        selectedItem = uiState.selectedItem,
        error = uiState.error,
        onNavigateBack = onNavigateBack,
        onAddItemClick = viewModel::onAddItemClick,
        onEditItemClick = viewModel::onEditItemClick,
        onDeleteItemClick = viewModel::onDeleteItemClick,
        onDialogDismiss = viewModel::onDialogDismiss,
        onSaveItem = { name, price ->
            viewModel.onSaveItem(
                id = uiState.selectedItem?.id ?: "",
                name = name,
                price = price
            )
        },
        onEventConsumed = viewModel::onEventConsumed,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreenContent(
    categoryName: String,
    items: List<MenuItem>,
    isLoading: Boolean,
    isDialogShown: Boolean,
    selectedItem: MenuItem?,
    error: String?,
    onNavigateBack: () -> Unit,
    onAddItemClick: () -> Unit,
    onEditItemClick: (MenuItem) -> Unit,
    onDeleteItemClick: (MenuItem) -> Unit,
    onDialogDismiss: () -> Unit,
    onSaveItem: (String, String) -> Unit,
    onEventConsumed: () -> Unit,
    modifier: Modifier = Modifier
) {
    var itemName by remember { mutableStateOf("") }
    var itemPrice by remember { mutableStateOf("") }

    // Reset form when dialog opens/closes
    if (!isDialogShown) {
        itemName = ""
        itemPrice = ""
    } else {
        itemName = selectedItem?.name ?: itemName
        itemPrice = selectedItem?.price ?: itemPrice
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            ComandaTopAppBar(
                title = categoryName,
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddItemClick,
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Item")
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            when {
                isLoading && items.isEmpty() -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                items.isEmpty() -> {
                    Text(
                        text = "No items in this category",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        items(items) { item ->
                            MenuItemCard(
                                item = item,
                                onEditClick = { onEditItemClick(item) },
                                onDeleteClick = { onDeleteItemClick(item) }
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }

    if (isDialogShown) {
        MenuItemDialog(
            itemName = itemName,
            itemPrice = itemPrice,
            onItemNameChange = { itemName = it },
            onItemPriceChange = { itemPrice = it },
            onDismiss = {
                onDialogDismiss()
                itemName = ""
                itemPrice = ""
            },
            onConfirm = {
                if (itemName.isNotBlank() && itemPrice.isNotBlank()) {
                    onSaveItem(itemName, itemPrice)
                    itemName = ""
                    itemPrice = ""
                }
            },
            isEditing = selectedItem != null
        )
    }
}

@Composable
private fun MenuItemCard(
    item: MenuItem,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = item.price,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            Row {
                IconButton(onClick = onEditClick) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
private fun MenuItemDialog(
    itemName: String,
    itemPrice: String,
    onItemNameChange: (String) -> Unit,
    onItemPriceChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    isEditing: Boolean
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (isEditing) "Edit Menu Item" else "Add Menu Item")
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = itemName,
                    onValueChange = onItemNameChange,
                    label = { Text("Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = itemPrice,
                    onValueChange = onItemPriceChange,
                    label = { Text("Price") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                enabled = itemName.isNotBlank() && itemPrice.isNotBlank()
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun CategoryScreenContentPreview() {
    ComandaTheme {
        CategoryScreenContent(
            categoryName = "Appetizers",
            items = listOf(
                MenuItem(id = "1", name = "Bruschetta", price = "€8.00"),
                MenuItem(id = "2", name = "Calamari", price = "€12.00")
            ),
            isLoading = false,
            isDialogShown = false,
            selectedItem = null,
            error = null,
            onNavigateBack = {},
            onAddItemClick = {},
            onEditItemClick = {},
            onDeleteItemClick = {},
            onDialogDismiss = {},
            onSaveItem = { _, _ -> },
            onEventConsumed = {}
        )
    }
}