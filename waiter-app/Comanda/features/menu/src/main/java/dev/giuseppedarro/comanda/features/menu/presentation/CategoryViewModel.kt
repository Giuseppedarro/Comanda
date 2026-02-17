package dev.giuseppedarro.comanda.features.menu.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.giuseppedarro.comanda.core.utils.toPriceCents
import dev.giuseppedarro.comanda.features.menu.domain.model.MenuItem
import dev.giuseppedarro.comanda.features.menu.domain.usecase.AddMenuItemUseCase
import dev.giuseppedarro.comanda.features.menu.domain.usecase.DeleteMenuItemUseCase
import dev.giuseppedarro.comanda.features.menu.domain.usecase.GetMenuUseCase
import dev.giuseppedarro.comanda.features.menu.domain.usecase.UpdateMenuItemUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

data class CategoryUiState(
    val categoryId: String = "",
    val categoryName: String = "",
    val items: List<MenuItem> = emptyList(),
    val isLoading: Boolean = false,
    val isDialogShown: Boolean = false,
    val selectedItem: MenuItem? = null,
    val error: String? = null
)

class CategoryViewModel(
    savedStateHandle: SavedStateHandle,
    private val getMenuUseCase: GetMenuUseCase,
    private val addMenuItemUseCase: AddMenuItemUseCase,
    private val updateMenuItemUseCase: UpdateMenuItemUseCase,
    private val deleteMenuItemUseCase: DeleteMenuItemUseCase
) : ViewModel() {

    private val categoryName: String = savedStateHandle.get<String>("categoryName") ?: ""

    private val _uiState = MutableStateFlow(CategoryUiState(categoryName = categoryName))
    val uiState: StateFlow<CategoryUiState> = _uiState.asStateFlow()

    init {
        loadCategory()
    }

    fun loadCategory() {
        viewModelScope.launch {
            getMenuUseCase()
                .onStart { _uiState.value = _uiState.value.copy(isLoading = true, error = null) }
                .collect { result ->
                    result.onSuccess {
                        val category = it.find { it.name == categoryName }
                        _uiState.value = _uiState.value.copy(
                            categoryId = category?.id ?: "",
                            items = category?.items ?: emptyList(),
                            isLoading = false,
                            error = null
                        )
                    }.onFailure {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = it.message
                        )
                    }
            }
        }
    }

    fun onAddItemClick() {
        _uiState.value = _uiState.value.copy(
            isDialogShown = true,
            selectedItem = null
        )
    }

    fun onEditItemClick(item: MenuItem) {
        _uiState.value = _uiState.value.copy(
            isDialogShown = true,
            selectedItem = item
        )
    }

    fun onDialogDismiss() {
        _uiState.value = _uiState.value.copy(
            isDialogShown = false,
            selectedItem = null
        )
    }

    fun onSaveItem(name: String, description: String, price: String, isAvailable: Boolean = true) {
        viewModelScope.launch {
            val priceInCents = price.toPriceCents()
            val categoryId = _uiState.value.categoryId

            val item = if (_uiState.value.selectedItem != null) {
                // Update existing item
                _uiState.value.selectedItem!!.copy(
                    name = name,
                    description = description,
                    price = priceInCents,
                    isAvailable = isAvailable
                )
            } else {
                // Create new item
                MenuItem(
                    id = "",
                    categoryId = categoryId,
                    name = name,
                    description = description,
                    price = priceInCents,
                    isAvailable = isAvailable,
                    displayOrder = _uiState.value.items.size
                )
            }

            val result = if (_uiState.value.selectedItem != null) {
                updateMenuItemUseCase(item)
            } else {
                addMenuItemUseCase(categoryId, item)
            }

            result.onSuccess {
                _uiState.value = _uiState.value.copy(
                    isDialogShown = false,
                    selectedItem = null,
                    error = null
                )
                loadCategory()
            }.onFailure {
                _uiState.value = _uiState.value.copy(
                    error = it.message
                )
            }
        }
    }

    fun onDeleteItemClick(item: MenuItem) {
        viewModelScope.launch {
            deleteMenuItemUseCase(item.id)
                .onSuccess { loadCategory() }
                .onFailure {
                    _uiState.value = _uiState.value.copy(
                        error = it.message
                    )
                }
        }
    }

    fun onEventConsumed() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}