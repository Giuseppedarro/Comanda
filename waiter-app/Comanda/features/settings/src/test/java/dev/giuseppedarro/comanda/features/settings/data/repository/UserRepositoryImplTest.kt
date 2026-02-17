package dev.giuseppedarro.comanda.features.settings.data.repository

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.giuseppedarro.comanda.features.settings.data.remote.UserApi
import dev.giuseppedarro.comanda.features.settings.data.remote.dto.CreateUserRequest
import dev.giuseppedarro.comanda.features.settings.data.remote.dto.UpdateUserRequest
import dev.giuseppedarro.comanda.features.settings.data.remote.dto.UserResponse
import dev.giuseppedarro.comanda.features.settings.domain.repository.UserRepository
import io.mockk.coEvery
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
        val userResponses: List<UserResponse> = listOf(
            UserResponse(
                id = 1,
                employeeId = "test-id",
                name = "test-name",
                role = "WAITER"
            )
        )
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
            assertThat(users[0].name).isEqualTo("test-name")
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
        val userResponse = UserResponse(1, "test-id", "test-name", "WAITER")
        val request = CreateUserRequest("test-id", "test-name", "password", "WAITER")
        coEvery { userApi.createUser(request) } returns Result.success(userResponse)

        // When
        val result = userRepository.createUser(request)

        // Then
        assertThat(result.isSuccess).isTrue()
        val user = result.getOrNull()
        assertThat(user).isNotNull()
        assertThat(user!!.id).isEqualTo("1")
        assertThat(user.name).isEqualTo("test-name")
    }

    @Test
    fun `createUser should return failure when api throws exception`() = runTest {
        // Given
        val exception = RuntimeException("Error")
        val request = CreateUserRequest("test-id", "test-name", "password", "WAITER")
        coEvery { userApi.createUser(request) } returns Result.failure(exception)

        // When
        val result = userRepository.createUser(request)

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
    }

    @Test
    fun `updateUser should return success when api is successful`() = runTest {
        // Given
        val userResponse = UserResponse(1, "test-id", "test-name", "WAITER")
        val request = UpdateUserRequest(name = "test-name", password = "password", role = "WAITER", employeeId = "test-id")
        coEvery { userApi.updateUser("1", request) } returns Result.success(userResponse)

        // When
        val result = userRepository.updateUser("1", request)

        // Then
        assertThat(result.isSuccess).isTrue()
        val user = result.getOrNull()
        assertThat(user).isNotNull()
        assertThat(user!!.id).isEqualTo("1")
        assertThat(user.name).isEqualTo("test-name")
    }

    @Test
    fun `updateUser should return failure when api throws exception`() = runTest {
        // Given
        val exception = RuntimeException("Error")
        val request = UpdateUserRequest(name = "test-name", password = "password", role = "WAITER", employeeId = "test-id")
        coEvery { userApi.updateUser("1", request) } returns Result.failure(exception)

        // When
        val result = userRepository.updateUser("1", request)

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
    }

    @Test
    fun `deleteUser should return success when api is successful`() = runTest {
        // Given
        coEvery { userApi.deleteUser("1") } returns Result.success(Unit)

        // When
        val result = userRepository.deleteUser("1")

        // Then
        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `deleteUser should return failure when api throws exception`() = runTest {
        // Given
        val exception = RuntimeException("Error")
        coEvery { userApi.deleteUser("1") } returns Result.failure(exception)

        // When
        val result = userRepository.deleteUser("1")

        // Then
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(exception)
    }
}