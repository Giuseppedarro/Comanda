package dev.giuseppedarro.comanda.features.users.domain.usecase

import dev.giuseppedarro.comanda.features.users.domain.model.User
import dev.giuseppedarro.comanda.features.users.domain.repository.UsersRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class GetUsersUseCaseTest {

    private val usersRepository = mockk<UsersRepository>()
    private val getUsersUseCase = GetUsersUseCase(usersRepository)

    @Test
    fun `should return list of users successfully`() = runTest {
        // Given
        val expectedUsers = listOf(
            User(1, "john.doe", "John Doe", "waiter"),
            User(2, "jane.doe", "Jane Doe", "admin")
        )
        
        coEvery { usersRepository.getUsers() } returns Result.success(expectedUsers)

        // When
        val result = getUsersUseCase()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedUsers, result.getOrNull())
        
        coVerify(exactly = 1) { usersRepository.getUsers() }
    }

    @Test
    fun `should return failure when repository fails`() = runTest {
        // Given
        val exception = Exception("Database error")
        coEvery { usersRepository.getUsers() } returns Result.failure(exception)

        // When
        val result = getUsersUseCase()

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
        
        coVerify(exactly = 1) { usersRepository.getUsers() }
    }
}
