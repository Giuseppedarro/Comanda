package dev.giuseppedarro.comanda.features.tables.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.giuseppedarro.comanda.features.tables.domain.model.Table
import dev.giuseppedarro.comanda.features.tables.domain.use_case.GetTablesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

data class TableOverviewUiState(
    val tables: List<Table> = emptyList(),
    val isLoading: Boolean = false, // Added for pull-to-refresh
    val isDialogShown: Boolean = false,
    val selectedTable: Table? = null
)

class TableOverviewViewModel(
    private val getTablesUseCase: GetTablesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TableOverviewUiState())
    val uiState: StateFlow<TableOverviewUiState> = _uiState.asStateFlow()

    init {
        loadTables()
    }

    // This function is now responsible for showing the loading state.
    private fun loadTables() {
        _uiState.update { it.copy(isLoading = true) } // Start loading
        getTablesUseCase().onEach { tables ->
            _uiState.update { it.copy(tables = tables, isLoading = false) } // End loading
        }.launchIn(viewModelScope)
    }

    // Public function to be called from the UI
    fun refresh() {
        loadTables()
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
}
