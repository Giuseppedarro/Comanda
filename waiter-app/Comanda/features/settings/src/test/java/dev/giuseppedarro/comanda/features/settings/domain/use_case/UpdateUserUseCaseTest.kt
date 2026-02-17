package dev.giuseppedarro.comanda.features.settings.domain.use_case

import com.google.common.truth.Truth.assertThat
import dev.giuseppedarro.comanda.features.settings.data.remote.dto.UpdateUserRequest
import dev.giuseppedarro.comanda.features.settings.domain.model.User
import dev.giuseppedarro.comanda.features.settings.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpdateUserUseCaseTest {

    private lateinit var updateUserUseCase: UpdateUserUseCase
    private lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        userRepository = mockk()
        updateUserUseCase = UpdateUserUseCase(userRepository)
    }

    @Test
    fun `invoke should call updateUser on repository`() = runTest {
        // Given
        val user = User("1", "test", "waiter")
        val request = UpdateUserRequest("test", "password", "waiter")
        coEvery { userRepository.updateUser("1", request) } returns Result.success(user)

        // When
        val result = updateUserUseCase("1", request)

        // Then
        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).isEqualTo(user)
    }
}