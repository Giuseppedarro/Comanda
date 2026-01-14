package dev.giuseppedarro.comanda.features.printers.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.giuseppedarro.comanda.core.utils.Result
import dev.giuseppedarro.comanda.features.printers.domain.model.Printer
import dev.giuseppedarro.comanda.features.printers.domain.use_case.CreatePrinterUseCase
import dev.giuseppedarro.comanda.features.printers.domain.use_case.DeletePrinterUseCase
import dev.giuseppedarro.comanda.features.printers.domain.use_case.GetAllPrintersUseCase
import dev.giuseppedarro.comanda.features.printers.domain.use_case.UpdatePrinterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PrinterManagementUiState(
    val printers: List<Printer> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isAddDialogOpen: Boolean = false,
    val isEditDialogOpen: Boolean = false,
    val editingPrinter: Printer? = null
)

class PrinterManagementViewModel(
    private val getAllPrintersUseCase: GetAllPrintersUseCase,
    private val createPrinterUseCase: CreatePrinterUseCase,
    private val updatePrinterUseCase: UpdatePrinterUseCase,
    private val deletePrinterUseCase: DeletePrinterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PrinterManagementUiState())
    val uiState: StateFlow<PrinterManagementUiState> = _uiState.asStateFlow()

    init {
        loadPrinters()
    }

    fun loadPrinters() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            when (val result = getAllPrintersUseCase()) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        printers = result.data ?: emptyList(),
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
                is Result.Loading -> {
                    // Already set loading at the start
                }
            }
        }
    }

    fun openAddDialog() {
        _uiState.value = _uiState.value.copy(
            isAddDialogOpen = true,
            error = null
        )
    }

    fun closeAddDialog() {
        _uiState.value = _uiState.value.copy(
            isAddDialogOpen = false
        )
    }

    fun openEditDialog(printer: Printer) {
        _uiState.value = _uiState.value.copy(
            isEditDialogOpen = true,
            editingPrinter = printer,
            error = null
        )
    }

    fun closeEditDialog() {
        _uiState.value = _uiState.value.copy(
            isEditDialogOpen = false,
            editingPrinter = null
        )
    }

    fun createPrinter(name: String, address: String, port: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            when (val result = createPrinterUseCase(name, address, port)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isAddDialogOpen = false,
                        error = null
                    )
                    loadPrinters() // Reload to get updated list
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                is Result.Loading -> {
                    // Already set loading at the start
                }
            }
        }
    }

    fun updatePrinter(id: Int, name: String, address: String, port: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            when (val result = updatePrinterUseCase(id, name, address, port)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isEditDialogOpen = false,
                        editingPrinter = null,
                        error = null
                    )
                    loadPrinters() // Reload to get updated list
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                is Result.Loading -> {
                    // Already set loading at the start
                }
            }
        }
    }

    fun deletePrinter(printer: Printer) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )

            when (val result = deletePrinterUseCase(printer.id)) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = null
                    )
                    loadPrinters() // Reload to get updated list
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                is Result.Loading -> {
                    // Already set loading at the start
                }
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(
            error = null
        )
    }
}