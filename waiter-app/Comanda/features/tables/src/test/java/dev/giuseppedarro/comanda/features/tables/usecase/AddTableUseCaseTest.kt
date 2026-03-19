package dev.giuseppedarro.comanda.features.tables.usecase

import dev.giuseppedarro.comanda.features.tables.domain.repository.TablesRepository
import dev.giuseppedarro.comanda.features.tables.domain.usecase.AddTableUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AddTableUseCaseTest {

    private lateinit var addTableUseCase: AddTableUseCase
    private val repository: TablesRepository = mockk()

    @Before
    fun setUp() {
        addTableUseCase = AddTableUseCase(repository)
    }

    @Test
    fun `when repository adds table successfully without number then return success`() = runBlocking {
        // Arrange
        coEvery { repository.addTable(null) } returns Result.success(Unit)

        // Act
        val result = addTableUseCase()

        // Assert
        assertTrue(result.isSuccess)
        coVerify { repository.addTable(null) }
    }

    @Test
    fun `when repository adds table successfully with number then return success`() = runBlocking {
        // Arrange
        val tableNumber = 5
        coEvery { repository.addTable(tableNumber) } returns Result.success(Unit)

        // Act
        val result = addTableUseCase(tableNumber)

        // Assert
        assertTrue(result.isSuccess)
        coVerify { repository.addTable(tableNumber) }
    }

    @Test
    fun `when repository fails to add table then return failure`() = runBlocking {
        // Arrange
        val exception = Exception("Failed to add table")
        coEvery { repository.addTable(any()) } returns Result.failure(exception)

        // Act
        val result = addTableUseCase(10)

        // Assert
        assertTrue(result.isFailure)
    }
}
