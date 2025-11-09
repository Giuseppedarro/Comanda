package dev.giuseppedarro.comanda.features.orders.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.giuseppedarro.comanda.core.utils.Result
import dev.giuseppedarro.comanda.features.orders.domain.model.MenuCategory
import dev.giuseppedarro.comanda.features.orders.domain.model.MenuItem
import dev.giuseppedarro.comanda.features.orders.domain.model.OrderItem
import dev.giuseppedarro.comanda.features.orders.domain.use_case.GetMenuUseCase
import dev.giuseppedarro.comanda.features.orders.domain.use_case.GetOrdersForTableUseCase
import dev.giuseppedarro.comanda.features.orders.domain.use_case.SubmitOrderUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val ARG_TABLE_NUMBER = "tableNumber"
private const val ARG_NUMBER_OF_PEOPLE = "numberOfPeople"

data class MenuOrderUiState(
    val orderItems: List<OrderItem> = emptyList(),
    val menuCategories: List<MenuCategory> = emptyList(),
    val selectedCategory: MenuCategory? = null,
    val isSheetVisible: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

class MenuOrderViewModel(
    savedStateHandle: SavedStateHandle,
    private val getMenu: GetMenuUseCase,
    private val getOrdersForTable: GetOrdersForTableUseCase,
    private val submitOrder: SubmitOrderUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MenuOrderUiState())
    val uiState: StateFlow<MenuOrderUiState> = _uiState.asStateFlow()

    private val tableNumber: Int = savedStateHandle[ARG_TABLE_NUMBER] ?: 0
    private val numberOfPeople: Int = savedStateHandle[ARG_NUMBER_OF_PEOPLE] ?: 0

    init {
        loadMenu()
        if (tableNumber > 0) {
            loadExistingOrder(tableNumber)
        }
    }

    private fun loadMenu() {
        viewModelScope.launch {
            getMenu().collect { res ->
                when (res) {
                    is Result.Loading -> _uiState.update { it.copy(isLoading = true) }
                    is Result.Success -> _uiState.update { it.copy(menuCategories = res.data.orEmpty(), isLoading = false, error = null) }
                    is Result.Error -> _uiState.update { it.copy(isLoading = false, error = res.message) }
                }
            }
        }
    }

    private fun loadExistingOrder(table: Int) {
        viewModelScope.launch {
            getOrdersForTable(table).collect { res ->
                when (res) {
                    is Result.Loading -> _uiState.update { it.copy(isLoading = true) }
                    is Result.Success -> _uiState.update { it.copy(orderItems = res.data.orEmpty(), isLoading = false, error = null) }
                    is Result.Error -> _uiState.update { it.copy(isLoading = false, error = res.message) }
                }
            }
        }
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

    fun onSendOrder(
        tableNumber: Int,
        numberOfPeople: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val items = _uiState.value.orderItems
        if (tableNumber <= 0 || numberOfPeople <= 0 || items.isEmpty()) {
            onError("Please select at least one item and enter valid table/people")
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val res = submitOrder(tableNumber, numberOfPeople, items)) {
                is Result.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    onSuccess()
                }
                is Result.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = res.message) }
                    onError(res.message ?: "Unknown error")
                }
                is Result.Loading -> {
                    // no-op, already set isLoading above
                }
            }
        }
    }
}
