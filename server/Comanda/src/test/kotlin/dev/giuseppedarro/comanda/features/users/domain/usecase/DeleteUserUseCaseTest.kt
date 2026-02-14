package dev.giuseppedarro.comanda.features.users.domain.usecase

import dev.giuseppedarro.comanda.features.users.domain.repository.UsersRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class DeleteUserUseCaseTest {

    private val usersRepository = mockk<UsersRepository>()
    private val deleteUserUseCase = DeleteUserUseCase(usersRepository)

    @Test
    fun `should delete user successfully`() = runTest {
        // Given
        val userId = 1
        coEvery { usersRepository.deleteUser(userId) } returns Result.success(Unit)

        // When
        val result = deleteUserUseCase(userId)

        // Then
        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { usersRepository.deleteUser(userId) }
    }
}
