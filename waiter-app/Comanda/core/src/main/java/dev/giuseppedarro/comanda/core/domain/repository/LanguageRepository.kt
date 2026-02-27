package dev.giuseppedarro.comanda.core.domain.repository

interface LanguageRepository {
    fun setApplicationLanguage(language: String)
    fun getApplicationLanguage(): String
}
