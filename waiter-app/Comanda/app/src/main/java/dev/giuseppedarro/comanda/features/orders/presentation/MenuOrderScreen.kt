package dev.giuseppedarro.comanda.features.orders.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import android.widget.Toast
import androidx.compose.material.icons.filled.Newspaper
import dev.giuseppedarro.comanda.core.presentation.ComandaTopAppBar
import dev.giuseppedarro.comanda.core.utils.toPriceStringUSD
import dev.giuseppedarro.comanda.features.orders.domain.model.MenuCategory
import dev.giuseppedarro.comanda.features.orders.domain.model.MenuItem
import dev.giuseppedarro.comanda.features.orders.domain.model.OrderItem
import dev.giuseppedarro.comanda.features.orders.presentation.components.MenuItemRow
import dev.giuseppedarro.comanda.ui.theme.ComandaTheme
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuOrderScreen(
    tableNumber: Int,
    numberOfPeople: Int,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MenuOrderViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    val context = LocalContext.current

    LaunchedEffect(uiState.isSheetVisible) {
        if (uiState.isSheetVisible) {
            sheetState.show()
        } else {
            sheetState.hide()
        }
    }

    MenuOrderContent(
        tableNumber = tableNumber,
        numberOfPeople = uiState.displayNumberOfPeople,
        uiState = uiState,
        onQuantityChange = viewModel::onQuantityChange,
        onCategorySelected = viewModel::onCategorySelected,
        onMenuItemAdded = viewModel::onMenuItemAdded,
        onDismissSheet = viewModel::onDismissSheet,
        onSendClick = {
            viewModel.onSendOrder(
                tableNumber = tableNumber,
                numberOfPeople = uiState.displayNumberOfPeople,
                onSuccess = {
                    Toast.makeText(context, "Order sent", Toast.LENGTH_SHORT).show()
                    onSendClick()
                },
                onError = { msg ->
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                }
            )
        },
        modifier = modifier,
        sheetState = sheetState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuOrderContent(
    tableNumber: Int,
    numberOfPeople: Int,
    uiState: MenuOrderUiState,
    onQuantityChange: (OrderItem, Int) -> Unit,
    onCategorySelected: (MenuCategory) -> Unit,
    onMenuItemAdded: (MenuItem) -> Unit,
    onDismissSheet: () -> Unit,
    onSendClick: () -> Unit,
    sheetState: androidx.compose.material3.SheetState,
    modifier: Modifier = Modifier
) {
    if (uiState.isSheetVisible && uiState.selectedCategory != null) {
        ModalBottomSheet(
            onDismissRequest = onDismissSheet,
            sheetState = sheetState,
        ) {
            LazyColumn {
                items(uiState.selectedCategory.items) { item ->
                    ListItem(
                        headlineContent = { Text(item.name) },
                        supportingContent = { Text(item.price.toPriceStringUSD()) },
                        modifier = Modifier.clickable { onMenuItemAdded(item) }
                    )
                }
            }
        }
    }

    val context = LocalContext.current

    Scaffold(
        modifier = modifier,
        topBar = {
            ComandaTopAppBar(
                title = "Table $tableNumber - $numberOfPeople People",
                actions = {
                    IconButton(onSendClick) {
                        Icon(Icons.Filled.Newspaper, contentDescription = "Send bill")
                    }
                    IconButton(onClick = onSendClick, enabled = !uiState.isLoading) {
                        Icon(Icons.Filled.Send, contentDescription = "Send order")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(uiState.orderItems) { item ->
                    MenuItemRow(
                        itemName = item.menuItem.name,
                        itemPrice = item.menuItem.price.toPriceStringUSD(),
                        quantity = item.quantity,
                        onQuantityChange = { newQuantity -> onQuantityChange(item, newQuantity) }
                    )
                }
            }

            Divider(modifier = Modifier.padding(horizontal = 16.dp)) // Added padding to this divider

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.weight(1f).padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.menuCategories) { category ->
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { onCategorySelected(category) },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Box(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) { 
                            Text(
                                text = category.name,
                                color = MaterialTheme.colorScheme.onSurface
                            ) 
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun MenuOrderScreenPreview() {
    val mockCategories = listOf(
        MenuCategory("Appetizers", emptyList()),
        MenuCategory("Main Courses", emptyList()),
        MenuCategory("Desserts", emptyList()),
        MenuCategory("Drinks", emptyList())
    )
    val mockOrderItems = listOf(
        OrderItem(MenuItem("1", "Gourmet Burger", 1299), 1),
        OrderItem(MenuItem("2", "Cola", 250), 2)
    )

    ComandaTheme {
        MenuOrderContent(
            tableNumber = 5,
            numberOfPeople = 4,
            uiState = MenuOrderUiState(
                orderItems = mockOrderItems,
                menuCategories = mockCategories
            ),
            onQuantityChange = { _, _ -> },
            onCategorySelected = {},
            onMenuItemAdded = {},
            onDismissSheet = {},
            onSendClick = {},
            sheetState = rememberModalBottomSheetState()
        )
    }
}
