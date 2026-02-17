package dev.giuseppedarro.comanda.features.menu.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import dev.giuseppedarro.comanda.core.utils.toPriceString
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.giuseppedarro.comanda.core.presentation.ComandaTopAppBar
import dev.giuseppedarro.comanda.core.ui.theme.ComandaTheme
import dev.giuseppedarro.comanda.features.menu.domain.model.MenuItem
import dev.giuseppedarro.comanda.features.menu.presentation.components.AddMenuItemDialog
import dev.giuseppedarro.comanda.features.menu.presentation.components.EditMenuItemDialog
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
        categoryId = uiState.categoryId,
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
        onSaveItem = { name, description, price, isAvailable ->
            viewModel.onSaveItem(
                name = name,
                description = description,
                price = price,
                isAvailable = isAvailable
            )
        },
        onRefresh = { viewModel.loadCategory() },
        onEventConsumed = viewModel::onEventConsumed,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun CategoryScreenContent(
    categoryId: String,
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
    onSaveItem: (String, String, String, Boolean) -> Unit,
    onRefresh: () -> Unit,
    onEventConsumed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pullRefreshState = rememberPullRefreshState(isLoading, onRefresh)

    Scaffold(
        modifier = modifier,
        topBar = {
            ComandaTopAppBar(
                title = categoryName,
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
                .pullRefresh(pullRefreshState)
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
                        items(
                            items = items,
                            key = { it.id}
                            ) { item ->
                            MenuItemCard(
                                modifier = Modifier.animateItem(),
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
            PullRefreshIndicator(
                refreshing = isLoading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }

    if (isDialogShown) {
        if (selectedItem == null) {
            AddMenuItemDialog(
                onDismiss = onDialogDismiss,
                onConfirm = { name, description, price, isAvailable ->
                    onSaveItem(name, description, price, isAvailable)
                }
            )
        } else {
            EditMenuItemDialog(
                menuItem = selectedItem,
                onDismiss = onDialogDismiss,
                onConfirm = { name, description, price, isAvailable ->
                    onSaveItem(name, description, price, isAvailable)
                }
            )
        }
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
        ),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
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
                    text = item.price.toPriceString(),
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

@Preview(showBackground = true)
@Composable
private fun CategoryScreenContentPreview() {
    ComandaTheme {
        CategoryScreenContent(
            categoryId = "1",
            categoryName = "Appetizers",
            items = listOf(
                MenuItem(id = "1", categoryId = "1", name = "Bruschetta", description = "Fresh bread with tomatoes", price = 800, isAvailable = true, displayOrder = 0),
                MenuItem(id = "2", categoryId = "1", name = "Calamari", description = "Fried squid", price = 1200, isAvailable = true, displayOrder = 1)
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
            onSaveItem = { _, _, _, _ -> },
            onRefresh = {},
            onEventConsumed = {}
        )
    }
}
