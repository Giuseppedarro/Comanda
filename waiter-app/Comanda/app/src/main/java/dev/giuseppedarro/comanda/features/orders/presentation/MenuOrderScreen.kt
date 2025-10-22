package dev.giuseppedarro.comanda.features.orders.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.giuseppedarro.comanda.core.presentation.ComandaTopAppBar
import dev.giuseppedarro.comanda.features.orders.presentation.components.MenuItemRow
import dev.giuseppedarro.comanda.ui.theme.ComandaTheme

data class MenuItem(
    val name: String,
    val price: String,
    val quantity: Int = 0,
    val isSelected: Boolean = false
)

@Composable
fun MenuOrderScreen(
    tableNumber: Int,
    numberOfPeople: Int,
    onProceedClick: () -> Unit,
    onBillOverviewClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var menuItems by remember {
        mutableStateOf(
            listOf(
                MenuItem("Gourmet Burger", "$12.99"),
                MenuItem("Caesar Salad", "$8.50"),
                MenuItem("Cappuccino", "$4.75")
            )
        )
    }

    MenuOrderContent(
        tableNumber = tableNumber,
        numberOfPeople = numberOfPeople,
        menuItems = menuItems,
        onQuantityChange = { index, newQuantity ->
            menuItems = menuItems.toMutableList().apply {
                this[index] = this[index].copy(quantity = newQuantity.coerceAtLeast(0))
            }
        },
        onItemSelected = { index, isSelected ->
            menuItems = menuItems.toMutableList().apply {
                this[index] = this[index].copy(isSelected = isSelected)
            }
        },
        onProceedClick = onProceedClick,
        onBillOverviewClick = onBillOverviewClick,
        modifier = modifier
    )
}

@Composable
fun MenuOrderContent(
    tableNumber: Int,
    numberOfPeople: Int,
    menuItems: List<MenuItem>,
    onQuantityChange: (Int, Int) -> Unit,
    onItemSelected: (Int, Boolean) -> Unit,
    onProceedClick: () -> Unit,
    onBillOverviewClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            ComandaTopAppBar(
                title = "Table $tableNumber - $numberOfPeople People",
                actions = {
                    Button(onClick = onBillOverviewClick) {
                        Text("Bill overview")
                    }
                }
            )
        },
        bottomBar = {
            Button(onClick = onProceedClick) {
                Text("Proceed to Order Summary")
            }
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            itemsIndexed(menuItems) { index, item ->
                MenuItemRow(
                    itemName = item.name,
                    itemPrice = item.price,
                    quantity = item.quantity,
                    onQuantityChange = { newQuantity -> onQuantityChange(index, newQuantity) },
                    onItemSelected = { isSelected -> onItemSelected(index, isSelected) },
                    isSelected = item.isSelected
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MenuOrderScreenPreview() {
    ComandaTheme {
        val menuItems = listOf(
            MenuItem("Gourmet Burger", "$12.99"),
            MenuItem("Caesar Salad", "$8.50"),
            MenuItem("Cappuccino", "$4.75")
        )
        MenuOrderContent(
            tableNumber = 5,
            numberOfPeople = 4,
            menuItems = menuItems,
            onQuantityChange = { _, _ -> },
            onItemSelected = { _, _ -> },
            onProceedClick = {},
            onBillOverviewClick = {}
        )
    }
}
