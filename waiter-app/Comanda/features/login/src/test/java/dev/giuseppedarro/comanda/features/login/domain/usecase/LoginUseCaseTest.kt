package dev.giuseppedarro.comanda.features.login.domain.usecase

import dev.giuseppedarro.comanda.features.login.domain.model.LoginException
import dev.giuseppedarro.comanda.features.login.domain.repository.LoginRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert
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
        coEvery { repository.login(any(), any()) } returns Result.success(Unit)

        val result = loginUseCase("id", "password")

        Assert.assertTrue(result.isSuccess)
    }

    @Test
    fun given_failed_login_then_return_failure_result() = runBlocking {
        val exception = LoginException.InvalidCredentials
        coEvery { repository.login(any(), any()) } returns Result.failure(exception)

        val result = loginUseCase("id", "wrong_password")

        Assert.assertTrue(result.isFailure)
        Assert.assertEquals(exception, result.exceptionOrNull())
    }

    @Test
    fun given_blank_employee_id_then_return_failure_with_EmptyCredentials() = runBlocking {
        val result = loginUseCase("", "password")

        Assert.assertTrue(result.isFailure)
        Assert.assertEquals(LoginException.EmptyCredentials, result.exceptionOrNull())
        coVerify(exactly = 0) { repository.login(any(), any()) }
    }

    @Test
    fun given_blank_password_then_return_failure_with_EmptyCredentials() = runBlocking {
        val result = loginUseCase("id", "")

        Assert.assertTrue(result.isFailure)
        Assert.assertEquals(LoginException.EmptyCredentials, result.exceptionOrNull())
        coVerify(exactly = 0) { repository.login(any(), any()) }
    }
}
