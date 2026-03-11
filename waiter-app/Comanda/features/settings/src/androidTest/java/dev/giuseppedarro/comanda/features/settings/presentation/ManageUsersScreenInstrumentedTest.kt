package dev.giuseppedarro.comanda.features.settings.presentation

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import dev.giuseppedarro.comanda.core.network.dto.CreateUserRequest
import dev.giuseppedarro.comanda.core.network.dto.UpdateUserRequest
import dev.giuseppedarro.comanda.core.ui.theme.ComandaTheme
import dev.giuseppedarro.comanda.features.settings.R
import dev.giuseppedarro.comanda.features.settings.domain.model.User
import dev.giuseppedarro.comanda.features.settings.domain.model.UserException
import dev.giuseppedarro.comanda.features.settings.domain.repository.UserRepository
import dev.giuseppedarro.comanda.features.settings.domain.usecase.CreateUserUseCase
import dev.giuseppedarro.comanda.features.settings.domain.usecase.DeleteUserUseCase
import dev.giuseppedarro.comanda.features.settings.domain.usecase.GetUsersUseCase
import dev.giuseppedarro.comanda.features.settings.domain.usecase.UpdateUserUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

class ManageUsersScreenInstrumentedTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun showsMappedErrorSnackbarWhenGetUsersFails() {
        val repository = FakeUserRepository(
            getUsersResult = flowOf(Result.failure(UserException.UserNotFound))
        )
        val viewModel = ManageUsersViewModel(
            createUserUseCase = CreateUserUseCase(repository),
            getUsersUseCase = GetUsersUseCase(repository),
            updateUserUseCase = UpdateUserUseCase(repository),
            deleteUserUseCase = DeleteUserUseCase(repository)
        )

        composeRule.setContent {
            ComandaTheme {
                ManageUsersScreen(
                    onBackClick = {},
                    viewModel = viewModel
                )
            }
        }

        val expected = composeRule.activity.getString(R.string.error_user_not_found)
        composeRule.waitForIdle()
        composeRule.onNodeWithText(expected).assertIsDisplayed()
    }

    private class FakeUserRepository(
        private val getUsersResult: Flow<Result<List<User>>> = flowOf(Result.success(emptyList()))
    ) : UserRepository {
        override suspend fun createUser(request: CreateUserRequest): Result<User> {
            return Result.success(User("1", request.employeeId, request.name, request.role))
        }

        override fun getUsers(): Flow<Result<List<User>>> = getUsersResult

        override suspend fun updateUser(id: String, request: UpdateUserRequest): Result<User> {
            return Result.success(
                User(
                    id = id,
                    employeeId = request.employeeId ?: "001",
                    name = request.name ?: "Updated",
                    role = request.role ?: "WAITER"
                )
            )
        }

        override suspend fun deleteUser(id: String): Result<Unit> = Result.success(Unit)
    }
}

