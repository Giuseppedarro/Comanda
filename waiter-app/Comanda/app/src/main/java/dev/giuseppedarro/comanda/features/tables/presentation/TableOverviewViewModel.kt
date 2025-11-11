package dev.giuseppedarro.comanda.features.tables.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.giuseppedarro.comanda.core.utils.Result
import dev.giuseppedarro.comanda.features.tables.domain.model.Table
import dev.giuseppedarro.comanda.features.tables.domain.use_case.AddTableUseCase
import dev.giuseppedarro.comanda.features.tables.domain.use_case.GetTablesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TableOverviewUiState(
    val tables: List<Table> = emptyList(),
    val isDialogShown: Boolean = false,
    val selectedTable: Table? = null,
    val isRefreshing: Boolean = false
)

class TableOverviewViewModel(
    private val getTablesUseCase: GetTablesUseCase,
    private val addTableUseCase: AddTableUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TableOverviewUiState())
    val uiState: StateFlow<TableOverviewUiState> = _uiState.asStateFlow()

    init {
        loadTables()
    }

    fun loadTables() {
        _uiState.update { it.copy(isRefreshing = true) }
        getTablesUseCase().onEach { tables ->
            _uiState.update { it.copy(tables = tables, isRefreshing = false) }
        }.launchIn(viewModelScope)
    }

    fun onTableClicked(table: Table) {
        if (table.isOccupied) {
            // TODO: Handle navigation for occupied table
        } else {
            _uiState.update { it.copy(isDialogShown = true, selectedTable = table) }
        }
    }

    fun onDialogDismiss() {
        _uiState.update { it.copy(isDialogShown = false, selectedTable = null) }
    }

    fun onDialogConfirm(numberOfPeople: Int) {
        // TODO: Handle navigation with the new order details
        // For now, just dismiss the dialog
        onDialogDismiss()
    }

    fun onAddTableClicked() {
        viewModelScope.launch {
            when (addTableUseCase()) {
                is Result.Success -> loadTables()
                is Result.Error -> {
                    // TODO: Show error message
                }
                else -> {}
            }
        }
    }
}
