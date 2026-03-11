package dev.giuseppedarro.comanda.features.settings.presentation

import com.google.common.truth.Truth.assertThat
import dev.giuseppedarro.comanda.core.domain.usecase.GetLanguageUseCase
import dev.giuseppedarro.comanda.core.domain.usecase.SetLanguageUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class LanguageSettingsViewModelTest {

    @Test
    fun `init sets useSystemLanguage true when repository returns empty language`() {
        val getLanguageUseCase = mockk<GetLanguageUseCase>()
        val setLanguageUseCase = mockk<SetLanguageUseCase>(relaxed = true)
        every { getLanguageUseCase() } returns ""

        val viewModel = LanguageSettingsViewModel(getLanguageUseCase, setLanguageUseCase)

        assertThat(viewModel.uiState.value.currentLanguage).isEmpty()
        assertThat(viewModel.uiState.value.useSystemLanguage).isTrue()
    }

    @Test
    fun `onLanguageChange updates state and delegates to use case`() {
        val getLanguageUseCase = mockk<GetLanguageUseCase>()
        val setLanguageUseCase = mockk<SetLanguageUseCase>(relaxed = true)
        every { getLanguageUseCase() } returns "en"

        val viewModel = LanguageSettingsViewModel(getLanguageUseCase, setLanguageUseCase)
        viewModel.onLanguageChange("it")

        assertThat(viewModel.uiState.value.currentLanguage).isEqualTo("it")
        verify(exactly = 1) { setLanguageUseCase("it") }
    }

    @Test
    fun `onUseSystemLanguageChange true clears language and enables system flag`() {
        val getLanguageUseCase = mockk<GetLanguageUseCase>()
        val setLanguageUseCase = mockk<SetLanguageUseCase>(relaxed = true)
        every { getLanguageUseCase() } returns "nl"

        val viewModel = LanguageSettingsViewModel(getLanguageUseCase, setLanguageUseCase)
        viewModel.onUseSystemLanguageChange(true)

        assertThat(viewModel.uiState.value.currentLanguage).isEmpty()
        assertThat(viewModel.uiState.value.useSystemLanguage).isTrue()
        verify(exactly = 1) { setLanguageUseCase("") }
    }

    @Test
    fun `onUseSystemLanguageChange false sets default english`() {
        val getLanguageUseCase = mockk<GetLanguageUseCase>()
        val setLanguageUseCase = mockk<SetLanguageUseCase>(relaxed = true)
        every { getLanguageUseCase() } returns ""

        val viewModel = LanguageSettingsViewModel(getLanguageUseCase, setLanguageUseCase)
        viewModel.onUseSystemLanguageChange(false)

        assertThat(viewModel.uiState.value.currentLanguage).isEqualTo("en")
        assertThat(viewModel.uiState.value.useSystemLanguage).isFalse()
        verify(exactly = 1) { setLanguageUseCase("en") }
    }
}

