package dev.giuseppedarro.comanda.features.login.presentation

import dev.giuseppedarro.comanda.features.login.domain.use_case.GetBaseUrlUseCase
import dev.giuseppedarro.comanda.features.login.domain.use_case.LoginUseCase
import dev.giuseppedarro.comanda.features.login.domain.use_case.SetBaseUrlUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var loginViewModel: LoginViewModel
    private val loginUseCase: LoginUseCase = mockk()
    private val getBaseUrlUseCase: GetBaseUrlUseCase = mockk()
    private val setBaseUrlUseCase: SetBaseUrlUseCase = mockk(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { getBaseUrlUseCase() } returns "http://default.url/"
        loginViewModel = LoginViewModel(loginUseCase, getBaseUrlUseCase, setBaseUrlUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun when_viewmodel_is_initialized_then_it_should_have_the_correct_initial_state() {
        val uiState = loginViewModel.uiState.value
        assertEquals("http://default.url/", uiState.baseUrl)
    }

    @Test
    fun when_onLoginClick_is_called_and_use_case_is_successful_then_uiState_should_reflect_success() = runTest {
        // Arrange
        coEvery { loginUseCase(any(), any()) } returns Result.success(Unit)

        // Act
        loginViewModel.onLoginClick()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val uiState = loginViewModel.uiState.value
        assertTrue(uiState.isLoginSuccessful)
    }

    @Test
    fun when_onLoginClick_is_called_and_use_case_fails_then_uiState_should_reflect_error() = runTest {
        // Arrange
        val errorMessage = "Invalid credentials"
        coEvery { loginUseCase(any(), any()) } returns Result.failure(Exception(errorMessage))

        // Act
        loginViewModel.onLoginClick()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        val uiState = loginViewModel.uiState.value
        assertEquals(errorMessage, uiState.errorMessage)
    }

    @Test
    fun when_onEmployeeIdChange_is_called_then_uiState_should_be_updated() {
        // Act
        loginViewModel.onEmployeeIdChange("new_id")

        // Assert
        assertEquals("new_id", loginViewModel.uiState.value.employeeId)
    }

    @Test
    fun when_onPasswordChange_is_called_then_uiState_should_be_updated() {
        // Act
        loginViewModel.onPasswordChange("new_password")

        // Assert
        assertEquals("new_password", loginViewModel.uiState.value.password)
    }

    @Test
    fun when_saveBaseUrl_is_called_then_it_should_call_the_use_case_and_refresh_the_url() {
        // Arrange
        val newUrl = "http://new.url/"
        every { getBaseUrlUseCase() } returns newUrl // Mock the refresh call

        // Act
        loginViewModel.saveBaseUrl()

        // Assert
        verify { setBaseUrlUseCase(any()) }
        assertEquals(newUrl, loginViewModel.uiState.value.baseUrl)
    }

    @Test
    fun when_onLoginHandled_is_called_then_isLoginSuccessful_should_be_false() = runTest {
        // Arrange
        coEvery { loginUseCase(any(), any()) } returns Result.success(Unit)
        loginViewModel.onLoginClick()
        testDispatcher.scheduler.advanceUntilIdle() // Ensure state is success

        // Act
        loginViewModel.onLoginHandled()

        // Assert
        assertFalse(loginViewModel.uiState.value.isLoginSuccessful)
    }
}