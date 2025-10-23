package dev.giuseppedarro.comanda.features.orders.presentation

import androidx.lifecycle.ViewModel
import dev.giuseppedarro.comanda.features.orders.domain.model.MenuCategory
import dev.giuseppedarro.comanda.features.orders.domain.model.MenuItem
import dev.giuseppedarro.comanda.features.orders.domain.model.OrderItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class MenuOrderUiState(
    val orderItems: List<OrderItem> = emptyList(),
    val menuCategories: List<MenuCategory> = emptyList(),
    val selectedCategory: MenuCategory? = null,
    val isSheetVisible: Boolean = false
)

class MenuOrderViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MenuOrderUiState())
    val uiState: StateFlow<MenuOrderUiState> = _uiState.asStateFlow()

    init {
        loadMenu()
    }

    private fun loadMenu() {
        val mockMenu = listOf(
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
        _uiState.update { it.copy(menuCategories = mockMenu) }
    }

    fun onCategorySelected(category: MenuCategory) {
        _uiState.update { it.copy(selectedCategory = category, isSheetVisible = true) }
    }

    fun onDismissSheet() {
        _uiState.update { it.copy(isSheetVisible = false, selectedCategory = null) }
    }

    fun onMenuItemAdded(menuItem: MenuItem) {
        val existingItem = _uiState.value.orderItems.find { it.menuItem.name == menuItem.name }
        val newOrderItems = if (existingItem != null) {
            _uiState.value.orderItems.map { if (it.menuItem.name == menuItem.name) it.copy(quantity = it.quantity + 1) else it }
        } else {
            _uiState.value.orderItems + OrderItem(menuItem)
        }
        // Hide sheet after selection
        _uiState.update { it.copy(orderItems = newOrderItems, isSheetVisible = false, selectedCategory = null) }
    }

    fun onQuantityChange(itemToChange: OrderItem, newQuantity: Int) {
        val newOrderItems = _uiState.value.orderItems.map {
            if (it.menuItem.name == itemToChange.menuItem.name) it.copy(quantity = newQuantity.coerceAtLeast(0)) else it
        }.filter { it.quantity > 0 }
        _uiState.update { it.copy(orderItems = newOrderItems) }
    }
}
