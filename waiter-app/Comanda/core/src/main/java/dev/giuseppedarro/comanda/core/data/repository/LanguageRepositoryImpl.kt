package dev.giuseppedarro.comanda.core.data.repository

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import dev.giuseppedarro.comanda.core.domain.repository.LanguageRepository

class LanguageRepositoryImpl : LanguageRepository {
    override fun setApplicationLanguage(language: String) {
        val localeList = LocaleListCompat.forLanguageTags(language)
        AppCompatDelegate.setApplicationLocales(localeList)
    }

    override fun getApplicationLanguage(): String {
        return AppCompatDelegate.getApplicationLocales().toLanguageTags()
    }
}
