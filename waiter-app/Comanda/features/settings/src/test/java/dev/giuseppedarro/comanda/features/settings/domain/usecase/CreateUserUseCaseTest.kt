package dev.giuseppedarro.comanda.features.settings.domain.usecase

import com.google.common.truth.Truth.assertThat
import dev.giuseppedarro.comanda.core.network.dto.CreateUserRequest
import dev.giuseppedarro.comanda.features.settings.domain.model.User
import dev.giuseppedarro.comanda.features.settings.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class CreateUserUseCaseTest {

    private lateinit var createUserUseCase: CreateUserUseCase
    private lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        userRepository = mockk()
        createUserUseCase = CreateUserUseCase(userRepository)
    }

    @Test
    fun `invoke should call repository and return result`() = runTest {
        // Given
        val request = CreateUserRequest("test-id", "test-name", "password", "WAITER")
        val expectedUser = User("1", "test-id", "test-name", "WAITER")
        coEvery { userRepository.createUser(request) } returns Result.success(expectedUser)

        // When
        val result = createUserUseCase(request)

        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(expectedUser)
    }
}
