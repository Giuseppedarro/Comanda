package dev.giuseppedarro.comanda.features.settings.domain.use_case

import com.google.common.truth.Truth.assertThat
import dev.giuseppedarro.comanda.features.settings.data.remote.dto.CreateUserRequest
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
    fun `invoke should call createUser on repository`() = runTest {
        // Given
        val user = User("1", "test", "test","waiter")
        val request = CreateUserRequest("test", "test", "password", "WAITER")
        coEvery { userRepository.createUser(request) } returns Result.success(user)

        // When
        val result = createUserUseCase(request)

        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(user)
    }
}