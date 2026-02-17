package dev.giuseppedarro.comanda.features.tables.presentation

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.giuseppedarro.comanda.core.domain.use_case.LogoutUseCase
import dev.giuseppedarro.comanda.features.tables.domain.model.Table
import dev.giuseppedarro.comanda.features.tables.domain.use_case.AddTableUseCase
import dev.giuseppedarro.comanda.features.tables.domain.use_case.GetTablesUseCase
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

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { getTablesUseCase() } returns flowOf(emptyList()) // Default mock
        viewModel = TableOverviewViewModel(getTablesUseCase, addTableUseCase, logoutUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when filter is ALL, all tables are shown`() = runTest {
        val tables = listOf(
            Table(1, true),
            Table(2, false)
        )
        every { getTablesUseCase() } returns flowOf(tables)

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
        every { getTablesUseCase() } returns flowOf(tables)

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
        every { getTablesUseCase() } returns flowOf(tables)

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
