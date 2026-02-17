package dev.giuseppedarro.comanda.features.tables.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.giuseppedarro.comanda.core.domain.use_case.LogoutUseCase
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

sealed interface TableOverviewEvent {
    data object LogoutSuccess : TableOverviewEvent
}

enum class TableFilter {
    ALL,
    OCCUPIED,
    AVAILABLE
}

data class TableOverviewUiState(
    val tables: List<Table> = emptyList(),
    val isDialogShown: Boolean = false,
    val selectedTable: Table? = null,
    val isRefreshing: Boolean = false,
    val filter: TableFilter = TableFilter.ALL
) {
    val filteredTables: List<Table>
        get() = when (filter) {
            TableFilter.OCCUPIED -> tables.filter { it.isOccupied }
            TableFilter.AVAILABLE -> tables.filter { !it.isOccupied }
            TableFilter.ALL -> tables
        }
}

class TableOverviewViewModel(
    private val getTablesUseCase: GetTablesUseCase,
    private val addTableUseCase: AddTableUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TableOverviewUiState())
    val uiState: StateFlow<TableOverviewUiState> = _uiState.asStateFlow()

    private val _event = MutableStateFlow<TableOverviewEvent?>(null)
    val event: StateFlow<TableOverviewEvent?> = _event.asStateFlow()

    init {
        loadTables()
    }

    fun loadTables() {
        _uiState.update { it.copy(isRefreshing = true) }
        getTablesUseCase().onEach { tables ->
            _uiState.update { it.copy(tables = tables, isRefreshing = false) }
        }.launchIn(viewModelScope)
    }

    fun onFilterChanged(filter: TableFilter) {
        _uiState.update { it.copy(filter = filter) }
    }

    fun onTableClicked(table: Table) {
        if (table.isOccupied) {
            // Occupied tables will be handled in a future update
        } else {
            _uiState.update { it.copy(isDialogShown = true, selectedTable = table) }
        }
    }

    fun onDialogDismiss() {
        _uiState.update { it.copy(isDialogShown = false, selectedTable = null) }
    }

    fun onDialogConfirm(numberOfPeople: Int) {
        onDialogDismiss()
    }

    fun onAddTableClicked() {
        viewModelScope.launch {
            addTableUseCase().onSuccess { loadTables() }
                .onFailure {
                    // Handle error
                }
        }
    }

    fun onLogout() {
        viewModelScope.launch {
            logoutUseCase().onSuccess { _event.value = TableOverviewEvent.LogoutSuccess }
                .onFailure {
                    // Handle error
                }
        }
    }

    fun onEventConsumed() {
        _event.value = null
    }
}