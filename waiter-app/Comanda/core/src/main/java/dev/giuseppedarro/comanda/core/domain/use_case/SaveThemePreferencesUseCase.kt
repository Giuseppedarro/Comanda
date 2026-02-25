
package dev.giuseppedarro.comanda.core.domain.use_case

import dev.giuseppedarro.comanda.core.domain.model.ThemePreferences
import dev.giuseppedarro.comanda.core.domain.repository.ThemeRepository

class SaveThemePreferencesUseCase(private val themeRepository: ThemeRepository) {
    suspend operator fun invoke(themePreferences: ThemePreferences) = themeRepository.saveThemePreferences(themePreferences)
}
