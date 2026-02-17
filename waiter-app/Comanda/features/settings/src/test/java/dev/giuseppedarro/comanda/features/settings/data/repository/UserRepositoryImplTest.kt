package dev.giuseppedarro.comanda.features.settings.data.repository

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.giuseppedarro.comanda.features.settings.data.remote.UserApi
import dev.giuseppedarro.comanda.features.settings.data.remote.dto.CreateUserRequest
import dev.giuseppedarro.comanda.features.settings.data.remote.dto.UpdateUserRequest
import dev.giuseppedarro.comanda.features.settings.data.remote.dto.UserResponse
import dev.giuseppedarro.comanda.features.settings.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UserRepositoryImplTest {

    private lateinit var userRepository: UserRepository
    private lateinit var userApi: UserApi

    @Before
    fun setUp() {
        userApi = mockk()
        userRepository = UserRepositoryImpl(userApi)
    }

    @Test
    fun `getUsers should return success when api is successful`() = runTest {
        // Given
        val userResponses = listOf(UserResponse("1", "test", "waiter"))
        coEvery { userApi.getUsers() } returns Result.success(userResponses)

        // When
        val result = userRepository.getUsers()

        // Then
        result.test {
            val emission = awaitItem()
            assertThat(emission.isSuccess).isTrue()
            val users = emission.getOrNull()
            assertThat(users).isNotNull()
            assertThat(users!!).hasSize(1)
            assertThat(users[0].id).isEqualTo("1")
            assertThat(users[0].username).isEqualTo("test")
            awaitComplete()
        }
    }

    @Test
    fun `getUsers should return failure when api throws exception`() = runTest {
        // Given
        val exception = RuntimeException("Error")
        coEvery { userApi.getUsers() } returns Result.failure(exception)

        // When
        val result = userRepository.getUsers()

        // Then
        result.test {
            val emission = awaitItem()
            assertThat(emission.isFailure).isTrue()
            assertThat(emission.exceptionOrNull()).isEqualTo(exception)
            awaitComplete()
        }
    }

    @Test
    fun `createUser should return success when api is successful`() = runTest {
        // Given
        val userResponse = UserResponse("1", "test", "waiter")
        coEvery { userApi.createUser(any()) } returns Result.success(userResponse)

        // When
        val result = userRepository.createUser(CreateUserRequest("test", "password", "waiter"))

        // Then
        assertThat(result.isSuccess).isTrue()
        val user = result.getOrNull()
        assertThat(user).isNotNull()
        assertThat(user!!.id).isEqualTo("1")
        assertThat(user.username).isEqualTo("test")
    }

    @Test
    fun `createUser should return failure when api throws exception`() = runTest {
        // Given
        val exception = RuntimeException("Error")
        coEvery { userApi.createUser(any()) } returns Result.failure(exception)

        // When
        val result = userRepository.createUser(CreateUserRequest("test", "password", "waiter"))

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
    }

    @Test
    fun `updateUser should return success when api is successful`() = runTest {
        // Given
        val userResponse = UserResponse("1", "test", "waiter")
        coEvery { userApi.updateUser(any(), any()) } returns Result.success(userResponse)

        // When
        val result = userRepository.updateUser("1", UpdateUserRequest("test", "password", "waiter"))

        // Then
        assertThat(result.isSuccess).isTrue()
        val user = result.getOrNull()
        assertThat(user).isNotNull()
        assertThat(user!!.id).isEqualTo("1")
        assertThat(user.username).isEqualTo("test")
    }

    @Test
    fun `updateUser should return failure when api throws exception`() = runTest {
        // Given
        val exception = RuntimeException("Error")
        coEvery { userApi.updateUser(any(), any()) } returns Result.failure(exception)

        // When
        val result = userRepository.updateUser("1", UpdateUserRequest("test", "password", "waiter"))

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
    }

    @Test
    fun `deleteUser should return success when api is successful`() = runTest {
        // Given
        coEvery { userApi.deleteUser(any()) } returns Result.success(Unit)

        // When
        val result = userRepository.deleteUser("1")

        // Then
        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `deleteUser should return failure when api throws exception`() = runTest {
        // Given
        val exception = RuntimeException("Error")
        coEvery { userApi.deleteUser(any()) } returns Result.failure(exception)

        // When
        val result = userRepository.deleteUser("1")

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
    }
}