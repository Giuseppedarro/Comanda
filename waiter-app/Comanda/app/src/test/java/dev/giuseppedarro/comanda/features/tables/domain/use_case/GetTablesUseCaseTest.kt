package dev.giuseppedarro.comanda.features.tables.domain.use_case

import app.cash.turbine.test
import dev.giuseppedarro.comanda.features.tables.domain.model.Table
import dev.giuseppedarro.comanda.features.tables.domain.repository.TablesRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetTablesUseCaseTest {

    private lateinit var getTablesUseCase: GetTablesUseCase
    private val repository: TablesRepository = mockk()

    @Before
    fun setUp() {
        getTablesUseCase = GetTablesUseCase(repository)
    }

    @Test
    fun when_repository_returns_tables_then_use_case_should_return_them() = runBlocking {
        // Arrange
        val tables = listOf(Table(1, false,))
        every { repository.getTables() } returns flowOf(tables)

        // Act & Assert
        getTablesUseCase().test {
            val emittedTables = awaitItem()
            assertEquals(tables, emittedTables)
            awaitComplete()
        }
    }
}