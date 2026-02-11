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

class CreateUserUseCaseTest {

    private val usersRepository = mockk<UsersRepository>()
    private val createUserUseCase = CreateUserUseCase(usersRepository)

    @Test
    fun `should hash password and create user successfully`() = runTest {
        // Given
        val params = CreateUserParams(
            employeeId = "john.doe",
            name = "John Doe",
            password = "securePassword123",
            role = "waiter"
        )
        
        val expectedUser = User(1, "john.doe", "John Doe", "waiter")
        
        // Capture the arguments passed to repository
        val passwordHashSlot = slot<String>()
        
        coEvery { 
            usersRepository.createUser(
                employeeId = params.employeeId,
                name = params.name,
                passwordHash = capture(passwordHashSlot),
                role = params.role
            ) 
        } returns Result.success(expectedUser)

        // When
        val result = createUserUseCase(params)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedUser, result.getOrNull())
        
        // Verify password was hashed
        val capturedHash = passwordHashSlot.captured
        assertTrue(capturedHash != params.password, "Password should be hashed, not plain text")
        assertTrue(BCrypt.checkpw(params.password, capturedHash), "Hash should match the original password")
        
        coVerify(exactly = 1) { 
            usersRepository.createUser(any(), any(), any(), any()) 
        }
    }

    @Test
    fun `should fail if password is too short`() = runTest {
        // Given
        val params = CreateUserParams(
            employeeId = "john.doe",
            name = "John Doe",
            password = "123", // Too short
            role = "waiter"
        )

        // When
        val result = createUserUseCase(params)

        // Then
        assertTrue(result.isFailure)
        assertEquals("Password must be at least 4 characters long", result.exceptionOrNull()?.message)
        
        coVerify(exactly = 0) { usersRepository.createUser(any(), any(), any(), any()) }
    }
}
