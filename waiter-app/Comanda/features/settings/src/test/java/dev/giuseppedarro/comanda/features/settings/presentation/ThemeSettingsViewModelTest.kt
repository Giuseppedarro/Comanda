
package dev.giuseppedarro.comanda.features.settings.presentation

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.giuseppedarro.comanda.core.domain.model.ThemePreferences
import dev.giuseppedarro.comanda.core.domain.usecase.GetThemePreferencesUseCase
import dev.giuseppedarro.comanda.core.domain.usecase.SaveThemePreferencesUseCase
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ThemeSettingsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getThemePreferencesUseCase: GetThemePreferencesUseCase
    private lateinit var saveThemePreferencesUseCase: SaveThemePreferencesUseCase
    private lateinit var viewModel: ThemeSettingsViewModel

    private val themePreferencesFlow = MutableStateFlow(ThemePreferences(false, false))

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getThemePreferencesUseCase = mockk()
        every { getThemePreferencesUseCase() } returns themePreferencesFlow

        saveThemePreferencesUseCase = mockk(relaxed = true)
        viewModel = ThemeSettingsViewModel(getThemePreferencesUseCase, saveThemePreferencesUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `uiState should reflect initial theme preferences`() = runTest {
        viewModel.uiState.test {
            val initialState = awaitItem()
            assertThat(initialState.useSystemTheme).isFalse()
            assertThat(initialState.isDarkMode).isFalse()
        }
    }

    @Test
    fun `uiState should update when theme preferences flow emits a new value`() = runTest {
        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(ThemeSettingsUiState(false, false))

            themePreferencesFlow.value = ThemePreferences(true, true)

            val updatedState = awaitItem()
            assertThat(updatedState.useSystemTheme).isTrue()
            assertThat(updatedState.isDarkMode).isTrue()
        }
    }

    @Test
    fun `onUseSystemThemeChange should call SaveThemePreferencesUseCase`() = runTest {
        viewModel.onUseSystemThemeChange(true)
        testDispatcher.scheduler.advanceUntilIdle()
        coVerify { saveThemePreferencesUseCase(ThemePreferences(true, false)) }
    }

    @Test
    fun `onDarkModeChange should call SaveThemePreferencesUseCase`() = runTest {
        viewModel.onDarkModeChange(true)
        testDispatcher.scheduler.advanceUntilIdle()
        coVerify { saveThemePreferencesUseCase(ThemePreferences(false, true)) }
    }
}
