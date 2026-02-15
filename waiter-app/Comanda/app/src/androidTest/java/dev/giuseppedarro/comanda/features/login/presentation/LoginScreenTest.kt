package dev.giuseppedarro.comanda.features.login.presentation

import android.content.Context
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import dev.giuseppedarro.comanda.R
import dev.giuseppedarro.comanda.features.login.domain.use_case.LoginUseCase
import dev.giuseppedarro.comanda.core.ui.theme.BrandedTheme
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context: Context = ApplicationProvider.getApplicationContext()

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
            BrandedTheme {
                LoginScreen(
                    onLoginSuccess = { },
                    viewModel = loginViewModel
                )
            }
        }

        // Act: Simulate a user clicking the sign-in button.
        composeTestRule.onNodeWithText(context.getString(R.string.sign_in)).performClick()
        composeTestRule.waitForIdle()

        // Assert: The error message from the mocked result is displayed.
        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }

    @Test
    fun whenLoading_thenProgressIndicatorIsShownAndFieldsAreDisabled() {
        composeTestRule.setContent {
            BrandedTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
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
        }

        composeTestRule.onNodeWithTag("loading_indicator").assertIsDisplayed()
        composeTestRule.onNodeWithText(context.getString(R.string.employee_id)).assertIsNotEnabled()
        composeTestRule.onNodeWithText(context.getString(R.string.password)).assertIsNotEnabled()
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
            BrandedTheme {
                LoginScreen(
                    onLoginSuccess = { loginSuccessCalled = true },
                    viewModel = loginViewModel
                )
            }
        }

        // Act: Simulate user input and a click.
        composeTestRule.onNodeWithText(context.getString(R.string.employee_id)).performTextInput("id")
        composeTestRule.onNodeWithText(context.getString(R.string.password)).performTextInput("password")
        composeTestRule.onNodeWithText(context.getString(R.string.sign_in)).performClick()

        composeTestRule.waitForIdle()

        // Assert: The onLoginSuccess callback was invoked.
        assertTrue(loginSuccessCalled)
    }

    @Test
    fun whenSavingBaseUrl_thenOnSaveBaseUrlIsCalled() {
        var onSaveBaseUrlCalled = false

        composeTestRule.setContent {
            BrandedTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
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
        }

        composeTestRule.onNodeWithContentDescription(context.getString(R.string.configure_server)).performClick()
        composeTestRule.onNodeWithText(context.getString(R.string.save)).performClick()

        assertTrue(onSaveBaseUrlCalled)
    }
}