package dev.giuseppedarro.comanda.features.tables.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// This will eventually move to the domain layer
data class Table(val number: Int, val isOccupied: Boolean)

data class TableOverviewUiState(
    val tables: List<Table> = emptyList(),
    val isDialogShown: Boolean = false,
    val selectedTable: Table? = null
)

class TableOverviewViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TableOverviewUiState())
    val uiState: StateFlow<TableOverviewUiState> = _uiState.asStateFlow()

    init {
        loadTables()
    }

    private fun loadTables() {
        // In the future, this will come from a repository
        val mockTables = List(10) { Table(number = it + 1, isOccupied = it % 2 == 0) }
        _uiState.update { it.copy(tables = mockTables) }
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