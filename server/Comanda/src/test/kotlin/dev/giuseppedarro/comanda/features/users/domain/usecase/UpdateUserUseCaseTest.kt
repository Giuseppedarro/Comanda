package dev.giuseppedarro.comanda.features.users.domain.usecase

import dev.giuseppedarro.comanda.features.users.domain.model.User
import dev.giuseppedarro.comanda.features.users.domain.repository.UsersRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mindrot.jbcrypt.BCrypt

class UpdateUserUseCaseTest {

    private val usersRepository = mockk<UsersRepository>()
    private val updateUserUseCase = UpdateUserUseCase(usersRepository)

    @Test
    fun `should hash password and update user successfully`() = runTest {
        // Given
        val params = UpdateUserParams(
            id = 1,
            name = "Updated Name",
            password = "newPassword123"
        )
        
        val expectedUser = User(1, "john.doe", "Updated Name", "waiter")
        
        val passwordHashSlot = slot<String?>()
        
        coEvery { 
            usersRepository.updateUser(
                id = params.id,
                employeeId = params.employeeId,
                name = params.name,
                passwordHash = captureNullable(passwordHashSlot),
                role = params.role
            ) 
        } returns Result.success(expectedUser)

        // When
        val result = updateUserUseCase(params)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedUser, result.getOrNull())
        
        // Verify password was hashed
        val capturedHash = passwordHashSlot.captured
        assertTrue(capturedHash != null)
        assertTrue(capturedHash != params.password)
        assertTrue(BCrypt.checkpw(params.password, capturedHash))
        
        coVerify(exactly = 1) { 
            usersRepository.updateUser(any(), any(), any(), any(), any()) 
        }
    }

    @Test
    fun `should update user without password if not provided`() = runTest {
        // Given
        val params = UpdateUserParams(
            id = 1,
            name = "Updated Name"
            // No password
        )
        
        val expectedUser = User(1, "john.doe", "Updated Name", "waiter")
        
        coEvery { 
            usersRepository.updateUser(
                id = params.id,
                employeeId = params.employeeId,
                name = params.name,
                passwordHash = null,
                role = params.role
            ) 
        } returns Result.success(expectedUser)

        // When
        val result = updateUserUseCase(params)

        // Then
        assertTrue(result.isSuccess)
        
        coVerify(exactly = 1) { 
            usersRepository.updateUser(
                id = 1, 
                employeeId = null, 
                name = "Updated Name", 
                passwordHash = null, 
                role = null
            ) 
        }
    }
}
