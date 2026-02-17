package dev.giuseppedarro.comanda.features.printers.presentation

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.giuseppedarro.comanda.features.printers.domain.model.Printer
import dev.giuseppedarro.comanda.features.printers.domain.use_case.CreatePrinterUseCase
import dev.giuseppedarro.comanda.features.printers.domain.use_case.DeletePrinterUseCase
import dev.giuseppedarro.comanda.features.printers.domain.use_case.GetAllPrintersUseCase
import dev.giuseppedarro.comanda.features.printers.domain.use_case.UpdatePrinterUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class PrinterManagementViewModelTest {

    private lateinit var viewModel: PrinterManagementViewModel
    private lateinit var getAllPrintersUseCase: GetAllPrintersUseCase
    private lateinit var createPrinterUseCase: CreatePrinterUseCase
    private lateinit var updatePrinterUseCase: UpdatePrinterUseCase
    private lateinit var deletePrinterUseCase: DeletePrinterUseCase

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getAllPrintersUseCase = mockk(relaxed = true)
        createPrinterUseCase = mockk(relaxed = true)
        updatePrinterUseCase = mockk(relaxed = true)
        deletePrinterUseCase = mockk(relaxed = true)

        coEvery { getAllPrintersUseCase() } returns Result.success(emptyList())

        viewModel = PrinterManagementViewModel(
            getAllPrintersUseCase = getAllPrintersUseCase,
            createPrinterUseCase = createPrinterUseCase,
            updatePrinterUseCase = updatePrinterUseCase,
            deletePrinterUseCase = deletePrinterUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `state is updated with printers when use case returns success`() = runTest {
        // Given
        val printers = listOf(Printer(1, "Kitchen Printer", "192.168.1.100", 9100))
        coEvery { getAllPrintersUseCase() } returns Result.success(printers)

        // When
        viewModel.loadPrinters()

        // Then
        viewModel.uiState.test {
            skipItems(1)
            val loadingState = awaitItem()
            assertThat(loadingState.isLoading).isTrue()

            val successState = awaitItem()
            assertThat(successState.isLoading).isFalse()
            assertThat(successState.printers).isEqualTo(printers)
            assertThat(successState.error).isNull()
        }
    }

    @Test
    fun `state is updated with error when use case returns failure`() = runTest {
        // Given
        val error = "Error"
        coEvery { getAllPrintersUseCase() } returns Result.failure(RuntimeException(error))

        // When
        viewModel.loadPrinters()

        // Then
        viewModel.uiState.test {
            skipItems(1)
            val loadingState = awaitItem()
            assertThat(loadingState.isLoading).isTrue()

            val errorState = awaitItem()
            assertThat(errorState.isLoading).isFalse()
            assertThat(errorState.printers).isEmpty()
            assertThat(errorState.error).isEqualTo(error)
        }
    }

    @Test
    fun `openAddDialog updates state to show add dialog`() = runTest {
        // When
        viewModel.openAddDialog()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.isAddDialogOpen).isTrue()
        }
    }

    @Test
    fun `closeAddDialog updates state to hide add dialog`() = runTest {
        // When
        viewModel.closeAddDialog()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.isAddDialogOpen).isFalse()
        }
    }

    @Test
    fun `openEditDialog updates state to show edit dialog`() = runTest {
        // Given
        val printer = Printer(1, "Kitchen Printer", "192.168.1.100", 9100)

        // When
        viewModel.openEditDialog(printer)

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.isEditDialogOpen).isTrue()
            assertThat(state.editingPrinter).isEqualTo(printer)
        }
    }

    @Test
    fun `closeEditDialog updates state to hide edit dialog`() = runTest {
        // When
        viewModel.closeEditDialog()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.isEditDialogOpen).isFalse()
            assertThat(state.editingPrinter).isNull()
        }
    }

    @Test
    fun `createPrinter calls createPrinterUseCase`() = runTest {
        // Given
        coEvery { createPrinterUseCase(any(), any(), any()) } returns Result.success(mockk())

        // When
        viewModel.createPrinter("Test", "1.1.1.1", 1111)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { createPrinterUseCase("Test", "1.1.1.1", 1111) }
    }

    @Test
    fun `updatePrinter calls updatePrinterUseCase`() = runTest {
        // Given
        coEvery { updatePrinterUseCase(any(), any(), any(), any()) } returns Result.success(mockk())

        // When
        viewModel.updatePrinter(1, "Test", "1.1.1.1", 1111)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { updatePrinterUseCase(1, "Test", "1.1.1.1", 1111) }
    }

    @Test
    fun `deletePrinter calls deletePrinterUseCase`() = runTest {
        // Given
        val printer = Printer(1, "Test", "1.1.1.1", 1111)
        coEvery { deletePrinterUseCase(any()) } returns Result.success(Unit)

        // When
        viewModel.deletePrinter(printer)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { deletePrinterUseCase(1) }
    }
}