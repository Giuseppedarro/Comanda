package dev.giuseppedarro.comanda.features.orders.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.giuseppedarro.comanda.core.presentation.UiText
import dev.giuseppedarro.comanda.features.orders.domain.model.MenuCategory
import dev.giuseppedarro.comanda.features.orders.domain.model.MenuItem
import dev.giuseppedarro.comanda.features.orders.domain.model.Order
import dev.giuseppedarro.comanda.features.orders.domain.model.OrderItem
import dev.giuseppedarro.comanda.features.orders.domain.usecase.GetMenuUseCase
import dev.giuseppedarro.comanda.features.orders.domain.usecase.GetOrdersForTableUseCase
import dev.giuseppedarro.comanda.features.orders.domain.usecase.PrintBillUseCase
import dev.giuseppedarro.comanda.features.orders.domain.usecase.SubmitOrderUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
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
    val error: UiText? = null,
    val existingOrder: Order? = null,
    val displayNumberOfPeople: Int = 0
)

class MenuOrderViewModel(
    savedStateHandle: SavedStateHandle,
    private val getMenu: GetMenuUseCase,
    private val getOrdersForTable: GetOrdersForTableUseCase,
    private val submitOrder: SubmitOrderUseCase,
    private val printBill: PrintBillUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MenuOrderUiState())
    val uiState: StateFlow<MenuOrderUiState> = _uiState.asStateFlow()

    private val tableNumber: Int = savedStateHandle[ARG_TABLE_NUMBER] ?: 0
    private val numberOfPeople: Int = savedStateHandle[ARG_NUMBER_OF_PEOPLE] ?: 0

    init {
        _uiState.update { it.copy(displayNumberOfPeople = numberOfPeople) }

        loadMenu()
        if (tableNumber > 0) {
            loadExistingOrder(tableNumber)
        }
    }

    private fun loadMenu() {
        viewModelScope.launch {
            getMenu()
                .onStart { _uiState.update { it.copy(isLoading = true) } }
                .collect { result ->
                    result.onSuccess { categories ->
                        _uiState.update {
                            it.copy(
                                menuCategories = categories,
                                isLoading = false,
                                error = null
                            )
                        }
                    }.onFailure { throwable ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = throwable.toOrderUiText()
                            )
                        }
                    }
            }
        }
    }

    private fun loadExistingOrder(table: Int) {
        viewModelScope.launch {
            getOrdersForTable(table)
                .onStart { _uiState.update { it.copy(isLoading = true) } }
                .collect { result ->
                    result.onSuccess { order ->
                        _uiState.update {
                            it.copy(
                                orderItems = order?.items ?: emptyList(),
                                existingOrder = order,
                                displayNumberOfPeople = order?.numberOfPeople ?: numberOfPeople,
                                isLoading = false,
                                error = null
                            )
                        }
                    }.onFailure { throwable ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = throwable.toOrderUiText()
                            )
                        }
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
        val existingItem = _uiState.value.orderItems.find { it.menuItem.id == menuItem.id }
        val newOrderItems = if (existingItem != null) {
            _uiState.value.orderItems.map { if (it.menuItem.id == menuItem.id) it.copy(quantity = it.quantity + 1) else it }
        } else {
            _uiState.value.orderItems + OrderItem(menuItem)
        }
        _uiState.update {
            it.copy(
                orderItems = newOrderItems,
                isSheetVisible = false,
                selectedCategory = null
            )
        }
    }

    fun onQuantityChange(itemToChange: OrderItem, newQuantity: Int) {
        val newOrderItems = _uiState.value.orderItems.map {
            if (it.menuItem.id == itemToChange.menuItem.id) it.copy(quantity = newQuantity.coerceAtLeast(0)) else it
        }.filter { it.quantity > 0 }
        _uiState.update { it.copy(orderItems = newOrderItems) }
    }

    fun onSendOrder(
        tableNumber: Int,
        numberOfPeople: Int,
        onSuccess: () -> Unit,
        onError: (UiText) -> Unit
    ) {
        val items = _uiState.value.orderItems
        val peopleCount = _uiState.value.displayNumberOfPeople

        if (tableNumber <= 0 || peopleCount <= 0 || items.isEmpty()) {
            val error = UiText.DynamicString("Please select at least one item and enter valid table/people")
            onError(error)
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            submitOrder(tableNumber, peopleCount, items)
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            existingOrder = null
                        )
                    }
                    onSuccess()
                }
                .onFailure { throwable ->
                    val errorUiText = throwable.toOrderUiText()
                    _uiState.update { it.copy(isLoading = false, error = errorUiText) }
                    onError(errorUiText)
                }
        }
    }

    fun onSendAndPrintBill(
        tableNumber: Int,
        numberOfPeople: Int,
        onSuccess: () -> Unit,
        onError: (UiText) -> Unit
    ) {
        val items = _uiState.value.orderItems
        val peopleCount = _uiState.value.displayNumberOfPeople

        if (tableNumber <= 0 || peopleCount <= 0 || items.isEmpty()) {
            val error = UiText.DynamicString("Please select at least one item and enter valid table/people")
            onError(error)
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            printBill(tableNumber, peopleCount, items)
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            existingOrder = null
                        )
                    }
                    onSuccess()
                }
                .onFailure { throwable ->
                    val errorUiText = throwable.toOrderUiText()
                    _uiState.update { it.copy(isLoading = false, error = errorUiText) }
                    onError(errorUiText)
                }
        }
    }

    fun onErrorConsumed() {
        _uiState.update { it.copy(error = null) }
    }
}
