package dev.giuseppedarro.comanda.features.settings.data.repository

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.giuseppedarro.comanda.core.domain.model.DomainException
import dev.giuseppedarro.comanda.core.domain.model.UserProfile
import dev.giuseppedarro.comanda.core.network.UserApi
import dev.giuseppedarro.comanda.core.network.dto.CreateUserRequest
import dev.giuseppedarro.comanda.core.network.dto.UpdateUserRequest
import dev.giuseppedarro.comanda.features.settings.domain.model.UserException
import dev.giuseppedarro.comanda.features.settings.domain.repository.UserRepository
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.mockk.coEvery
import io.mockk.every
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
        val userProfiles: List<UserProfile> = listOf(
            UserProfile(
                id = 1,
                employeeId = "test-id",
                name = "test-name",
                role = "WAITER"
            )
        )
        coEvery { userApi.getUsers() } returns userProfiles

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
        val exception = RuntimeException("Error")
        coEvery { userApi.getUsers() } throws exception

        val result = userRepository.getUsers()

        result.test {
            val emission = awaitItem()
            assertThat(emission.isFailure).isTrue()
            assertThat(emission.exceptionOrNull()).isEqualTo(DomainException.UnknownException("Error"))
            awaitComplete()
        }
    }

    @Test
    fun `createUser should return success when api is successful`() = runTest {
        // Given
        val userProfile = UserProfile(1, "test-id", "test-name", "WAITER")
        val request = CreateUserRequest("test-id", "test-name", "password", "WAITER")
        coEvery { userApi.createUser(request) } returns userProfile

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
        val exception = RuntimeException("Error")
        val request = CreateUserRequest("test-id", "test-name", "password", "WAITER")
        coEvery { userApi.createUser(request) } throws exception

        val result = userRepository.createUser(request)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(DomainException.UnknownException("Error"))
    }

    @Test
    fun `updateUser should return success when api is successful`() = runTest {
        // Given
        val userProfile = UserProfile(1, "test-id", "test-name", "WAITER")
        val request = UpdateUserRequest(name = "test-name", password = "password", role = "WAITER", employeeId = "test-id")
        coEvery { userApi.updateUser(1, request) } returns userProfile

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
        val exception = RuntimeException("Error")
        val request = UpdateUserRequest(name = "test-name", password = "password", role = "WAITER", employeeId = "test-id")
        coEvery { userApi.updateUser(1, request) } throws exception

        val result = userRepository.updateUser("1", request)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(DomainException.UnknownException("Error"))
    }

    @Test
    fun `deleteUser should return success when api is successful`() = runTest {
        // Given
        coEvery { userApi.deleteUser(1) } returns Unit

        // When
        val result = userRepository.deleteUser("1")

        // Then
        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `deleteUser should return failure when api throws exception`() = runTest {
        val exception = RuntimeException("Error")
        coEvery { userApi.deleteUser(1) } throws exception

        val result = userRepository.deleteUser("1")

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(DomainException.UnknownException("Error"))
    }

    @Test
    fun `createUser maps 409 to DuplicateEmployeeId`() = runTest {
        val request = CreateUserRequest("test-id", "test-name", "password", "WAITER")
        val response = mockk<HttpResponse>(relaxed = true)
        every { response.status } returns HttpStatusCode.Conflict
        val exception = ClientRequestException(response, "")

        coEvery { userApi.createUser(request) } throws exception

        val result = userRepository.createUser(request)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(UserException.DuplicateEmployeeId)
    }

    @Test
    fun `updateUser maps 404 to UserNotFound`() = runTest {
        val request = UpdateUserRequest(name = "test-name", password = "password", role = "WAITER", employeeId = "test-id")
        val response = mockk<HttpResponse>(relaxed = true)
        every { response.status } returns HttpStatusCode.NotFound
        val exception = ClientRequestException(response, "")

        coEvery { userApi.updateUser(1, request) } throws exception

        val result = userRepository.updateUser("1", request)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(UserException.UserNotFound)
    }

    @Test
    fun `deleteUser maps 422 to InvalidUserData`() = runTest {
        val response = mockk<HttpResponse>(relaxed = true)
        every { response.status } returns HttpStatusCode.UnprocessableEntity
        val exception = ClientRequestException(response, "")

        coEvery { userApi.deleteUser(1) } throws exception

        val result = userRepository.deleteUser("1")

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(UserException.InvalidUserData)
    }

    @Test
    fun `updateUser with non numeric id returns InvalidUserId`() = runTest {
        val request = UpdateUserRequest(name = "test-name", password = "password", role = "WAITER", employeeId = "test-id")

        val result = userRepository.updateUser("abc", request)

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(UserException.InvalidUserId)
    }

    @Test
    fun `deleteUser with non numeric id returns InvalidUserId`() = runTest {
        val result = userRepository.deleteUser("abc")

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()).isEqualTo(UserException.InvalidUserId)
    }
}
