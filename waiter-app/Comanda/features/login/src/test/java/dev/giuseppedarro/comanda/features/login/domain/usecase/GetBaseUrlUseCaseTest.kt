package dev.giuseppedarro.comanda.features.login.domain.usecase

import dev.giuseppedarro.comanda.core.network.BaseUrlProvider
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GetBaseUrlUseCaseTest {

    private lateinit var getBaseUrlUseCase: GetBaseUrlUseCase
    private val baseUrlProvider: BaseUrlProvider = mockk()

    @Before
    fun setUp() {
        getBaseUrlUseCase = GetBaseUrlUseCase(baseUrlProvider)
    }

    @Test
    fun when_use_case_is_invoked_then_it_should_return_the_url_from_the_provider() {
        // Arrange
        val expectedUrl = "http://test.url/"
        every { baseUrlProvider.getBaseUrl() } returns expectedUrl

        // Act
        val result = getBaseUrlUseCase()

        // Assert
        Assert.assertEquals(expectedUrl, result)
    }
}