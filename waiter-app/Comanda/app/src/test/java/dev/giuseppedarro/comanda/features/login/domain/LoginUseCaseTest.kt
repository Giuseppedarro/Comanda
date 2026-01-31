package dev.giuseppedarro.comanda.features.login.domain

import dev.giuseppedarro.comanda.features.login.domain.repository.LoginRepository
import dev.giuseppedarro.comanda.features.login.domain.use_case.LoginUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LoginUseCaseTest {

    private lateinit var loginUseCase: LoginUseCase
    private val repository: LoginRepository = mockk()

    @Before
    fun setUp() {
        loginUseCase = LoginUseCase(repository)
    }

    @Test
    fun given_successful_login_then_return_success_result() = runBlocking {
        // Arrange
        coEvery { repository.login(any(), any()) } returns Result.success(Unit)

        // Act
        val result = loginUseCase("id", "password")

        // Assert
        assertTrue(result.isSuccess)
    }

    @Test
    fun given_failed_login_then_return_failure_result() = runBlocking {
        // Arrange
        val exception = Exception("Invalid credentials")
        coEvery { repository.login(any(), any()) } returns Result.failure(exception)

        // Act
        val result = loginUseCase("id", "wrong_password")

        // Assert
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message == exception.message)
    }

    @Test
    fun given_blank_employee_id_then_return_failure() = runBlocking {
        // Act
        val result = loginUseCase("", "password")

        // Assert
        assertTrue(result.isFailure)
        coVerify(exactly = 0) { repository.login(any(), any()) }
    }

    @Test
    fun given_blank_password_then_return_failure() = runBlocking {
        // Act
        val result = loginUseCase("id", "")

        // Assert
        assertTrue(result.isFailure)
        coVerify(exactly = 0) { repository.login(any(), any()) }
    }
}