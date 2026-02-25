
package dev.giuseppedarro.comanda.core.domain.use_case

import dev.giuseppedarro.comanda.core.domain.repository.ThemeRepository

class GetThemePreferencesUseCase(private val themeRepository: ThemeRepository) {
    operator fun invoke() = themeRepository.getThemePreferences()
}
