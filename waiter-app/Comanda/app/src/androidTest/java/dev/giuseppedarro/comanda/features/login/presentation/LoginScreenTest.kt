package dev.giuseppedarro.comanda.features.login.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import dev.giuseppedarro.comanda.features.login.domain.use_case.GetBaseUrlUseCase
import dev.giuseppedarro.comanda.features.login.domain.use_case.LoginUseCase
import dev.giuseppedarro.comanda.features.login.domain.use_case.SetBaseUrlUseCase
import dev.giuseppedarro.comanda.ui.theme.ComandaTheme
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun whenLoginFails_thenErrorMessageIsShown() {
        // Arrange: Mock the use case to return a failure result.
        val errorMessage = "Invalid credentials"
        val loginUseCase = mockk<LoginUseCase>()
        coEvery { loginUseCase(any(), any()) } returns Result.failure(Exception(errorMessage))

        val loginViewModel = LoginViewModel(
            loginUseCase = loginUseCase,
            getBaseUrl = mockk(relaxed = true),
            setBaseUrl = mockk(relaxed = true)
        )

        composeTestRule.setContent {
            ComandaTheme {
                LoginScreen(
                    onLoginSuccess = { },
                    viewModel = loginViewModel
                )
            }
        }

        // Act: Simulate a user clicking the sign-in button.
        composeTestRule.onNodeWithText("Sign In").performClick()
        composeTestRule.waitForIdle()

        // Assert: The error message from the mocked result is displayed.
        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }

    @Test
    fun whenLoading_thenProgressIndicatorIsShownAndFieldsAreDisabled() {
        composeTestRule.setContent {
            ComandaTheme {
                LoginContent(
                    uiState = LoginUiState(isLoading = true),
                    onEmployeeIdChange = {},
                    onPasswordChange = {},
                    onLoginClick = {},
                    baseUrl = "",
                    onBaseUrlChange = {},
                    onSaveBaseUrl = {}
                )
            }
        }

        composeTestRule.onNodeWithTag("loading_indicator").assertIsDisplayed()
        composeTestRule.onNodeWithText("Employee ID").assertIsNotEnabled()
        composeTestRule.onNodeWithText("Password").assertIsNotEnabled()
    }

    @Test
    fun whenLoginSucceeds_thenOnLoginSuccessIsCalled() {
        // Arrange: Mock the use case to return a success result.
        var loginSuccessCalled = false
        val loginUseCase = mockk<LoginUseCase>()
        coEvery { loginUseCase(any(), any()) } returns Result.success(Unit)

        val loginViewModel = LoginViewModel(
            loginUseCase = loginUseCase,
            getBaseUrl = mockk(relaxed = true),
            setBaseUrl = mockk(relaxed = true)
        )

        composeTestRule.setContent {
            ComandaTheme {
                LoginScreen(
                    onLoginSuccess = { loginSuccessCalled = true },
                    viewModel = loginViewModel
                )
            }
        }

        // Act: Simulate user input and a click.
        composeTestRule.onNodeWithText("Employee ID").performTextInput("id")
        composeTestRule.onNodeWithText("Password").performTextInput("password")
        composeTestRule.onNodeWithText("Sign In").performClick()

        composeTestRule.waitForIdle()

        // Assert: The onLoginSuccess callback was invoked.
        assertTrue(loginSuccessCalled)
    }

    @Test
    fun whenSavingBaseUrl_thenOnSaveBaseUrlIsCalled() {
        var onSaveBaseUrlCalled = false

        composeTestRule.setContent {
            ComandaTheme {
                LoginContent(
                    uiState = LoginUiState(),
                    onEmployeeIdChange = {},
                    onPasswordChange = {},
                    onLoginClick = {},
                    baseUrl = "",
                    onBaseUrlChange = {},
                    onSaveBaseUrl = { onSaveBaseUrlCalled = true }
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Configure server").performClick()
        composeTestRule.onNodeWithText("Save").performClick()

        assertTrue(onSaveBaseUrlCalled)
    }
}