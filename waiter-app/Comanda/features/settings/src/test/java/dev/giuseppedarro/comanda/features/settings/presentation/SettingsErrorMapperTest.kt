package dev.giuseppedarro.comanda.features.settings.presentation

import com.google.common.truth.Truth.assertThat
import dev.giuseppedarro.comanda.core.R as CoreR
import dev.giuseppedarro.comanda.core.domain.model.DomainException
import dev.giuseppedarro.comanda.core.presentation.UiText
import dev.giuseppedarro.comanda.features.settings.R
import dev.giuseppedarro.comanda.features.settings.domain.model.UserException
import org.junit.Test

class SettingsErrorMapperTest {

    @Test
    fun `maps DuplicateEmployeeId to feature string resource`() {
        val uiText = UserException.DuplicateEmployeeId.toSettingsUiText()

        assertThat(uiText).isInstanceOf(UiText.StringResource::class.java)
        assertThat((uiText as UiText.StringResource).resId).isEqualTo(R.string.error_user_duplicate_employee_id)
    }

    @Test
    fun `maps UserNotFound to feature string resource`() {
        val uiText = UserException.UserNotFound.toSettingsUiText()

        assertThat(uiText).isInstanceOf(UiText.StringResource::class.java)
        assertThat((uiText as UiText.StringResource).resId).isEqualTo(R.string.error_user_not_found)
    }

    @Test
    fun `maps InvalidUserData to feature string resource`() {
        val uiText = UserException.InvalidUserData.toSettingsUiText()

        assertThat(uiText).isInstanceOf(UiText.StringResource::class.java)
        assertThat((uiText as UiText.StringResource).resId).isEqualTo(R.string.error_user_invalid_data)
    }

    @Test
    fun `maps InvalidUserId to feature string resource`() {
        val uiText = UserException.InvalidUserId.toSettingsUiText()

        assertThat(uiText).isInstanceOf(UiText.StringResource::class.java)
        assertThat((uiText as UiText.StringResource).resId).isEqualTo(R.string.error_user_invalid_id)
    }

    @Test
    fun `maps core domain exception through core mapper`() {
        val uiText = DomainException.UnauthorizedException.toSettingsUiText()

        assertThat(uiText).isInstanceOf(UiText.StringResource::class.java)
        assertThat((uiText as UiText.StringResource).resId).isEqualTo(CoreR.string.error_unauthorized)
    }

    @Test
    fun `maps generic throwable with message to dynamic string`() {
        val uiText = IllegalStateException("boom").toSettingsUiText()

        assertThat(uiText).isInstanceOf(UiText.DynamicString::class.java)
        assertThat((uiText as UiText.DynamicString).value).isEqualTo("boom")
    }
}

