package dev.giuseppedarro.comanda.core.domain.usecase

import dev.giuseppedarro.comanda.core.domain.repository.LanguageRepository

class SetLanguageUseCase(private val languageRepository: LanguageRepository) {
    operator fun invoke(language: String) = languageRepository.setApplicationLanguage(language)
}
