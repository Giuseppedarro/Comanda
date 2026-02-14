package dev.giuseppedarro.comanda.features.tables.domain.use_case

import dev.giuseppedarro.comanda.features.tables.domain.repository.TablesRepository
import io.mockk.coEvery
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
    fun when_repository_adds_table_successfully_then_return_success() = runBlocking {
        // Arrange
        coEvery { repository.addTable() } returns Result.success(Unit)

        // Act
        val result = addTableUseCase()

        // Assert
        assertTrue(result.isSuccess)
    }

    @Test
    fun when_repository_fails_to_add_table_then_return_failure() = runBlocking {
        // Arrange
        val exception = Exception("Failed to add table")
        coEvery { repository.addTable() } returns Result.failure(exception)

        // Act
        val result = addTableUseCase()

        // Assert
        assertTrue(result.isFailure)
    }
}