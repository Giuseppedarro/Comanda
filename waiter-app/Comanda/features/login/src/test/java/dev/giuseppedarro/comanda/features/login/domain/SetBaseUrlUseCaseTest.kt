package dev.giuseppedarro.comanda.features.login.domain

import dev.giuseppedarro.comanda.core.network.BaseUrlProvider
import dev.giuseppedarro.comanda.features.login.domain.use_case.SetBaseUrlUseCase
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class SetBaseUrlUseCaseTest {

    private lateinit var setBaseUrlUseCase: SetBaseUrlUseCase
    private val baseUrlProvider: BaseUrlProvider = mockk(relaxed = true)

    @Before
    fun setUp() {
        setBaseUrlUseCase = SetBaseUrlUseCase(baseUrlProvider)
    }

    @Test
    fun given_url_without_scheme_or_slash_then_sanitizes_correctly() {
        // Arrange
        val newUrl = "test.url"
        val sanitizedUrl = "http://test.url/"

        // Act
        setBaseUrlUseCase(newUrl)

        // Assert
        verify { baseUrlProvider.setBaseUrl(sanitizedUrl) }
    }

    @Test
    fun given_url_with_http_and_no_slash_then_adds_slash() {
        // Arrange
        val newUrl = "http://test.url"
        val sanitizedUrl = "http://test.url/"

        // Act
        setBaseUrlUseCase(newUrl)

        // Assert
        verify { baseUrlProvider.setBaseUrl(sanitizedUrl) }
    }

    @Test
    fun given_a_perfectly_formatted_https_url_then_remains_unchanged() {
        // Arrange
        val newUrl = "https://test.url/"

        // Act
        setBaseUrlUseCase(newUrl)

        // Assert
        verify { baseUrlProvider.setBaseUrl(newUrl) }
    }

    @Test
    fun given_a_url_with_whitespace_then_trims_it_correctly() {
        // Arrange
        val newUrl = "  http://test.url/  "
        val sanitizedUrl = "http://test.url/"

        // Act
        setBaseUrlUseCase(newUrl)

        // Assert
        verify { baseUrlProvider.setBaseUrl(sanitizedUrl) }
    }
}