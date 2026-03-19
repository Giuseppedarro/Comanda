package dev.giuseppedarro.comanda.features.tables.domain.usecase

import dev.giuseppedarro.comanda.features.tables.domain.model.Table
import dev.giuseppedarro.comanda.features.tables.domain.repository.TablesRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class AddTableUseCaseTest {

    private val tablesRepository = mockk<TablesRepository>()
    private val addTableUseCase = AddTableUseCase(tablesRepository)

    @Test
    fun `should return success with the new table when number is available`() = runTest {
        // Given
        val expectedTable = Table(number = 5, isOccupied = false)
        coEvery { tablesRepository.addTable(5) } returns Result.success(expectedTable)

        // When
        val result = addTableUseCase(5)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedTable, result.getOrNull())
        coVerify(exactly = 1) { tablesRepository.addTable(5) }
    }

    @Test
    fun `should return failure when table number already exists`() = runTest {
        // Given
        val error = IllegalArgumentException("Table with number 3 already exists")
        coEvery { tablesRepository.addTable(3) } returns Result.failure(error)

        // When
        val result = addTableUseCase(3)

        // Then
        assertTrue(result.isFailure)
        assertEquals(error.message, result.exceptionOrNull()?.message)
        coVerify(exactly = 1) { tablesRepository.addTable(3) }
    }

    @Test
    fun `should delegate to repository with the provided number`() = runTest {
        // Given
        val expectedTable = Table(number = 10, isOccupied = false)
        coEvery { tablesRepository.addTable(10) } returns Result.success(expectedTable)

        // When
        addTableUseCase(10)

        // Then
        coVerify(exactly = 1) { tablesRepository.addTable(10) }
    }

    @Test
    fun `should auto-increment when no number is provided`() = runTest {
        // Given
        val expectedTable = Table(number = 6, isOccupied = false)
        coEvery { tablesRepository.addTable(null) } returns Result.success(expectedTable)

        // When
        val result = addTableUseCase()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedTable, result.getOrNull())
        coVerify(exactly = 1) { tablesRepository.addTable(null) }
    }
}

