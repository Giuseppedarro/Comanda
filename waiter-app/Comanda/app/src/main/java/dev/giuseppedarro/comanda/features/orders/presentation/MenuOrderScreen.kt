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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.giuseppedarro.comanda.core.presentation.ComandaTopAppBar
import dev.giuseppedarro.comanda.features.orders.presentation.components.MenuItemRow
import dev.giuseppedarro.comanda.ui.theme.ComandaTheme

// --- Data Models ---
data class MenuItem(val name: String, val price: String)
data class OrderItem(val menuItem: MenuItem, val quantity: Int = 1)
data class MenuCategory(val name: String, val items: List<MenuItem>)

// --- Mock Data ---
private val mockMenu = listOf(
    MenuCategory("Appetizers", listOf(
        MenuItem("Bruschetta", "$7.00"),
        MenuItem("Garlic Bread", "$5.00"),
        MenuItem("Stuffed Mushrooms", "$8.50"),
        MenuItem("Spring Rolls", "$6.00"),
        MenuItem("Onion Rings", "$5.50"),
        MenuItem("Calamari", "$9.00")
    )),
    MenuCategory("Main Courses", listOf(MenuItem("Gourmet Burger", "$12.99"), MenuItem("Caesar Salad", "$8.50"))),
    MenuCategory("Desserts", listOf(MenuItem("Tiramisu", "$6.50"), MenuItem("Cheesecake", "$7.50"))),
    MenuCategory("Drinks", listOf(
        MenuItem("Cola", "$2.50"),
        MenuItem("Cappuccino", "$4.75"),
        MenuItem("Iced Tea", "$2.00"),
        MenuItem("Orange Juice", "$3.00"),
        MenuItem("Latte", "$4.00"),
        MenuItem("Water", "$1.00"),
        MenuItem("Espresso", "$3.00"),
        MenuItem("Lemonade", "$3.50"),
        MenuItem("Apple Juice", "$3.00"),
        MenuItem("Sparkling Water", "$1.50"),
        MenuItem("Green Tea", "$2.50"),
        MenuItem("Beer", "$5.00")
    ))
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuOrderScreen(
    tableNumber: Int,
    numberOfPeople: Int,
    onProceedClick: () -> Unit,
    onBillOverviewClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var orderItems by remember { mutableStateOf<List<OrderItem>>(emptyList()) }
    val sheetState = rememberModalBottomSheetState()
    var selectedCategory by remember { mutableStateOf<MenuCategory?>(null) }

    LaunchedEffect(selectedCategory) {
        if (selectedCategory != null) {
            sheetState.show()
        } else {
            sheetState.hide()
        }
    }

    MenuOrderContent(
        tableNumber = tableNumber,
        numberOfPeople = numberOfPeople,
        orderItems = orderItems,
        onQuantityChange = { itemToChange, newQuantity ->
            orderItems = orderItems.map { 
                if (it.menuItem.name == itemToChange.menuItem.name) it.copy(quantity = newQuantity.coerceAtLeast(0)) else it
            }.filter { it.quantity > 0 }
        },
        onCategorySelected = { category ->
            selectedCategory = category
        },
        onMenuItemAdded = { menuItem ->
            val existingItem = orderItems.find { it.menuItem.name == menuItem.name }
            orderItems = if (existingItem != null) {
                orderItems.map { if (it.menuItem.name == menuItem.name) it.copy(quantity = it.quantity + 1) else it }
            } else {
                orderItems + OrderItem(menuItem)
            }
            selectedCategory = null // Hide sheet after selection
        },
        onDismissSheet = { selectedCategory = null },
        onProceedClick = onProceedClick,
        onBillOverviewClick = onBillOverviewClick,
        modifier = modifier,
        sheetState = sheetState,
        selectedCategory = selectedCategory
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuOrderContent(
    tableNumber: Int,
    numberOfPeople: Int,
    orderItems: List<OrderItem>,
    onQuantityChange: (OrderItem, Int) -> Unit,
    onCategorySelected: (MenuCategory) -> Unit,
    onMenuItemAdded: (MenuItem) -> Unit,
    onDismissSheet: () -> Unit,
    onProceedClick: () -> Unit,
    onBillOverviewClick: () -> Unit,
    sheetState: androidx.compose.material3.SheetState,
    selectedCategory: MenuCategory?,
    modifier: Modifier = Modifier
) {
    if (selectedCategory != null) {
        ModalBottomSheet(
            onDismissRequest = onDismissSheet,
            sheetState = sheetState,
        ) {
            // Content of the Bottom Sheet
            LazyColumn {
                items(selectedCategory.items) { item ->
                    ListItem(
                        headlineContent = { Text(item.name) },
                        supportingContent = { Text(item.price) },
                        modifier = Modifier.clickable { onMenuItemAdded(item) }
                    )
                }
            }
        }
    }
    
    Scaffold(
        modifier = modifier,
        topBar = { ComandaTopAppBar(title = "Table $tableNumber - $numberOfPeople People") },
        bottomBar = { Button(onClick = onProceedClick) { Text("Proceed to Order Summary") } }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            // Top half: Current order list
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(orderItems) { item ->
                    MenuItemRow(
                        itemName = item.menuItem.name,
                        itemPrice = item.menuItem.price,
                        quantity = item.quantity,
                        onQuantityChange = { newQuantity -> onQuantityChange(item, newQuantity) }
                    )
                }
            }

            // Bottom half: Category grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.weight(1f).padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(mockMenu) { category ->
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { onCategorySelected(category) }
                    ) {
                        Box(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) { Text(text = category.name) }
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
    ComandaTheme {
        MenuOrderContent(
            tableNumber = 5,
            numberOfPeople = 4,
            orderItems = listOf(OrderItem(MenuItem("Gourmet Burger", "$12.99"), 1)),
            onQuantityChange = { _, _ -> },
            onCategorySelected = {},
            onMenuItemAdded = {},
            onDismissSheet = {},
            onProceedClick = {},
            onBillOverviewClick = {},
            sheetState = rememberModalBottomSheetState(),
            selectedCategory = null
        )
    }
}
