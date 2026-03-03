package dev.giuseppedarro.comanda.core.domain.usecase

import dev.giuseppedarro.comanda.core.domain.repository.ThemeRepository

class GetThemePreferencesUseCase(private val themeRepository: ThemeRepository) {
    operator fun invoke() = themeRepository.getThemePreferences()
}
