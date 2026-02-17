package dev.giuseppedarro.comanda.features.settings.domain.use_case

import com.google.common.truth.Truth.assertThat
import dev.giuseppedarro.comanda.features.settings.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DeleteUserUseCaseTest {

    private lateinit var deleteUserUseCase: DeleteUserUseCase
    private lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        userRepository = mockk()
        deleteUserUseCase = DeleteUserUseCase(userRepository)
    }

    @Test
    fun `invoke should call deleteUser on repository`() = runTest {
        // Given
        coEvery { userRepository.deleteUser("1") } returns Result.success(Unit)

        // When
        val result = deleteUserUseCase("1")

        // Then
        assertThat(result.isSuccess).isTrue()
    }
}