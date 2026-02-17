package dev.giuseppedarro.comanda.features.settings.domain.use_case

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.giuseppedarro.comanda.features.settings.domain.model.User
import dev.giuseppedarro.comanda.features.settings.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetUsersUseCaseTest {

    private lateinit var getUsersUseCase: GetUsersUseCase
    private lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        userRepository = mockk()
        getUsersUseCase = GetUsersUseCase(userRepository)
    }

    @Test
    fun `invoke should return users from repository`() = runTest {
        // Given
        val users = listOf(User("1", "test", "waiter"))
        coEvery { userRepository.getUsers() } returns flowOf(Result.success(users))

        // When
        val result = getUsersUseCase()

        // Then
        result.test {
            val emission = awaitItem()
            assertThat(emission.isSuccess).isTrue()
            assertThat(emission.getOrNull()).isEqualTo(users)
            awaitComplete()
        }
    }
}