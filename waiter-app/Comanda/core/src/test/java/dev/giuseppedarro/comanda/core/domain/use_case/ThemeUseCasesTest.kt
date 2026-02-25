
package dev.giuseppedarro.comanda.core.domain.use_case

import dev.giuseppedarro.comanda.core.domain.model.ThemePreferences
import dev.giuseppedarro.comanda.core.domain.repository.ThemeRepository
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ThemeUseCasesTest {

    private lateinit var mockThemeRepository: ThemeRepository
    private lateinit var getThemePreferencesUseCase: GetThemePreferencesUseCase
    private lateinit var saveThemePreferencesUseCase: SaveThemePreferencesUseCase

    @Before
    fun setup() {
        mockThemeRepository = mockk(relaxed = true)
        getThemePreferencesUseCase = GetThemePreferencesUseCase(mockThemeRepository)
        saveThemePreferencesUseCase = SaveThemePreferencesUseCase(mockThemeRepository)
    }

    @Test
    fun `GetThemePreferencesUseCase should call getThemePreferences on repository`() {
        // Given
        every { mockThemeRepository.getThemePreferences() } returns flowOf()

        // When
        getThemePreferencesUseCase()

        // Then
        verify { mockThemeRepository.getThemePreferences() }
    }

    @Test
    fun `SaveThemePreferencesUseCase should call saveThemePreferences on repository`() = runTest {
        // Given
        val themePreferences = ThemePreferences(useSystemTheme = true, isDarkMode = false)

        // When
        saveThemePreferencesUseCase(themePreferences)

        // Then
        coVerify { mockThemeRepository.saveThemePreferences(themePreferences) }
    }
}
