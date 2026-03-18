package dev.giuseppedarro.comanda.features.tables.presentation

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.giuseppedarro.comanda.core.domain.usecase.GetCurrentUserUseCase
import dev.giuseppedarro.comanda.core.domain.usecase.LogoutUseCase
import dev.giuseppedarro.comanda.core.domain.usecase.SyncUserProfileUseCase
import dev.giuseppedarro.comanda.features.tables.domain.model.Table
import dev.giuseppedarro.comanda.features.tables.domain.usecase.AddTableUseCase
import dev.giuseppedarro.comanda.features.tables.domain.usecase.GetTablesUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
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
    private val logoutUseCase: LogoutUseCase = mockk()
    private val getCurrentUserUseCase: GetCurrentUserUseCase = mockk()
    private val syncUserProfileUseCase: SyncUserProfileUseCase = mockk()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { getTablesUseCase() } returns flowOf(Result.success(emptyList()))
        every { getCurrentUserUseCase() } returns flowOf(null)
        coEvery { syncUserProfileUseCase() } returns Result.success(mockk())
        
        viewModel = TableOverviewViewModel(
            getTablesUseCase,
            addTableUseCase,
            logoutUseCase,
            getCurrentUserUseCase,
            syncUserProfileUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when onAddTableClicked is called, show add table dialog`() = runTest {
        viewModel.onAddTableClicked()
        
        assertThat(viewModel.uiState.value.isAddTableDialogShown).isTrue()
    }

    @Test
    fun `when onConfirmAddTable is called without number, call use case with null`() = runTest {
        coEvery { addTableUseCase(null) } returns Result.success(Unit)
        
        viewModel.onConfirmAddTable(null)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { addTableUseCase(null) }
        assertThat(viewModel.uiState.value.isAddTableDialogShown).isFalse()
    }

    @Test
    fun `when onConfirmAddTable is called with number, call use case with number`() = runTest {
        val number = 42
        coEvery { addTableUseCase(number) } returns Result.success(Unit)
        
        viewModel.onConfirmAddTable(number)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { addTableUseCase(number) }
        assertThat(viewModel.uiState.value.isAddTableDialogShown).isFalse()
    }

    @Test
    fun `when filter is ALL, all tables are shown`() = runTest {
        val tables = listOf(
            Table(1, true),
            Table(2, false)
        )
        every { getTablesUseCase() } returns flowOf(Result.success(tables))

        viewModel.loadTables()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onFilterChanged(TableFilter.ALL)

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.filteredTables).hasSize(2)
        }
    }

    @Test
    fun `when filter is OCCUPIED, only occupied tables are shown`() = runTest {
        val tables = listOf(
            Table(1, true),
            Table(2, false)
        )
        every { getTablesUseCase() } returns flowOf(Result.success(tables))

        viewModel.loadTables()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onFilterChanged(TableFilter.OCCUPIED)

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.filteredTables).hasSize(1)
            assertThat(state.filteredTables.first().isOccupied).isTrue()
        }
    }

    @Test
    fun `when filter is AVAILABLE, only available tables are shown`() = runTest {
        val tables = listOf(
            Table(1, true),
            Table(2, false)
        )
        every { getTablesUseCase() } returns flowOf(Result.success(tables))

        viewModel.loadTables()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onFilterChanged(TableFilter.AVAILABLE)

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.filteredTables).hasSize(1)
            assertThat(state.filteredTables.first().isOccupied).isFalse()
        }
    }
}
