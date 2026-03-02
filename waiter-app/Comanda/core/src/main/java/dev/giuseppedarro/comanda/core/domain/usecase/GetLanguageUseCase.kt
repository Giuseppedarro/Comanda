package dev.giuseppedarro.comanda.core.domain.usecase

import dev.giuseppedarro.comanda.core.domain.repository.LanguageRepository

class GetLanguageUseCase(private val languageRepository: LanguageRepository) {
    operator fun invoke() = languageRepository.getApplicationLanguage()
}
