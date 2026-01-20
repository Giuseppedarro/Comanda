package dev.giuseppedarro.comanda.features.menu.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.giuseppedarro.comanda.core.utils.Result
import dev.giuseppedarro.comanda.features.menu.domain.model.MenuCategory
import dev.giuseppedarro.comanda.features.menu.domain.usecase.GetMenuUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MenuUiState(
    val categories: List<MenuCategory> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class MenuViewModel(
    private val getMenuUseCase: GetMenuUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MenuUiState())
    val uiState: StateFlow<MenuUiState> = _uiState.asStateFlow()

    init {
        loadMenu()
    }

    fun loadMenu() {
        viewModelScope.launch {
            getMenuUseCase().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = true,
                            error = null
                        )
                    }
                    is Result.Success -> {
                        _uiState.value = _uiState.value.copy(
                            categories = result.data ?: emptyList(),
                            isLoading = false,
                            error = null
                        )
                    }
                    is Result.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
            }
        }
    }
}