package dev.giuseppedarro.comanda.features.tables.presentation

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.giuseppedarro.comanda.core.utils.Result
import dev.giuseppedarro.comanda.features.tables.domain.model.Table
import dev.giuseppedarro.comanda.features.tables.domain.use_case.AddTableUseCase
import dev.giuseppedarro.comanda.features.tables.domain.use_case.GetTablesUseCase
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
class TableOverviewViewModelTest {

    private lateinit var viewModel: TableOverviewViewModel
    private val getTablesUseCase: GetTablesUseCase = mockk()
    private val addTableUseCase: AddTableUseCase = mockk()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun loadTables_updatesStateWithTables_onSuccess() = runTest {
        // Given
        val tables = listOf(Table(1, false))
        coEvery { getTablesUseCase() } returns flowOf(tables)

        // When
        viewModel = TableOverviewViewModel(getTablesUseCase, addTableUseCase)
        testScheduler.advanceUntilIdle() // Allow the flow to emit

        // Then
        viewModel.uiState.test {
            val emission = awaitItem()
            assertThat(emission.tables).isEqualTo(tables)
            assertThat(emission.isRefreshing).isFalse()
        }
    }

    @Test
    fun onTableClicked_updatesStateToShowDialog_forUnoccupiedTable() = runTest {
        // Given
        coEvery { getTablesUseCase() } returns flowOf(emptyList())
        viewModel = TableOverviewViewModel(getTablesUseCase, addTableUseCase)
        val table = Table(number = 2, isOccupied = false)

        // When
        viewModel.onTableClicked(table)

        // Then
        viewModel.uiState.test {
            val emission = awaitItem()
            assertThat(emission.isDialogShown).isTrue()
            assertThat(emission.selectedTable).isEqualTo(table)
        }
    }

    @Test
    fun onDialogDismiss_updatesStateToHideDialog() = runTest {
        // Given
        coEvery { getTablesUseCase() } returns flowOf(emptyList())
        viewModel = TableOverviewViewModel(getTablesUseCase, addTableUseCase)
        viewModel.onTableClicked(Table(2, false)) // Show the dialog first

        // When
        viewModel.onDialogDismiss()

        // Then
        viewModel.uiState.test {
            val emission = awaitItem()
            assertThat(emission.isDialogShown).isFalse()
            assertThat(emission.selectedTable).isNull()
        }
    }

    @Test
    fun onAddTableClicked_callsUseCaseAndReloadsTables_onSuccess() = runTest {
        // Given
        coEvery { getTablesUseCase() } returns flowOf(emptyList()) // Initial load
        coEvery { addTableUseCase() } returns Result.Success(Unit)
        viewModel = TableOverviewViewModel(getTablesUseCase, addTableUseCase)

        // When
        viewModel.onAddTableClicked()
        testScheduler.advanceUntilIdle() // Ensure the coroutine completes

        // Then
        coVerify(exactly = 1) { addTableUseCase() }
        coVerify(exactly = 2) { getTablesUseCase() } // 1 initial, 1 after add
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}