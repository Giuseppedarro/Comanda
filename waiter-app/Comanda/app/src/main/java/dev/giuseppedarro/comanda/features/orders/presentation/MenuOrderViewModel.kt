package dev.giuseppedarro.comanda.features.orders.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.giuseppedarro.comanda.core.utils.Result
import dev.giuseppedarro.comanda.features.orders.domain.model.MenuCategory
import dev.giuseppedarro.comanda.features.orders.domain.model.MenuItem
import dev.giuseppedarro.comanda.features.orders.domain.model.OrderItem
import dev.giuseppedarro.comanda.features.orders.domain.use_case.GetMenuUseCase
import dev.giuseppedarro.comanda.features.orders.domain.use_case.SubmitOrderUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MenuOrderUiState(
    val orderItems: List<OrderItem> = emptyList(),
    val menuCategories: List<MenuCategory> = emptyList(),
    val selectedCategory: MenuCategory? = null,
    val isSheetVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class MenuOrderViewModel(
    private val getMenuUseCase: GetMenuUseCase,
    private val submitOrderUseCase: SubmitOrderUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(MenuOrderUiState())
    val uiState: StateFlow<MenuOrderUiState> = _uiState.asStateFlow()

    init {
        loadMenu()
    }

    private fun loadMenu() {
        getMenuUseCase().onEach { result ->
            when (result) {
                is Result.Loading -> {
                    _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                }
                is Result.Success -> {
                    _uiState.update { it.copy(isLoading = false, menuCategories = result.data ?: emptyList()) }
                }
                is Result.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun submitOrder() {
        viewModelScope.launch {
            val tableNumber = savedStateHandle.get<Int>("tableNumber") ?: -1
            val result = submitOrderUseCase(tableNumber, _uiState.value.orderItems)
            // Handle result, e.g., show a success/error message
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
        _uiState.update { it.copy(orderItems = newOrderItems, isSheetVisible = false, selectedCategory = null) }
    }

    fun onQuantityChange(itemToChange: OrderItem, newQuantity: Int) {
        val newOrderItems = _uiState.value.orderItems.map {
            if (it.menuItem.name == itemToChange.menuItem.name) it.copy(quantity = newQuantity.coerceAtLeast(0)) else it
        }.filter { it.quantity > 0 }
        _uiState.update { it.copy(orderItems = newOrderItems) }
    }
}
