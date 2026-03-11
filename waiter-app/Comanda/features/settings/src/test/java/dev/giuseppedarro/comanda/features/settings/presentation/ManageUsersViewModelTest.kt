package dev.giuseppedarro.comanda.features.settings.presentation

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.giuseppedarro.comanda.core.presentation.UiText
import dev.giuseppedarro.comanda.features.settings.R
import dev.giuseppedarro.comanda.features.settings.domain.model.User
import dev.giuseppedarro.comanda.features.settings.domain.model.UserException
import dev.giuseppedarro.comanda.features.settings.domain.usecase.CreateUserUseCase
import dev.giuseppedarro.comanda.features.settings.domain.usecase.DeleteUserUseCase
import dev.giuseppedarro.comanda.features.settings.domain.usecase.GetUsersUseCase
import dev.giuseppedarro.comanda.features.settings.domain.usecase.UpdateUserUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ManageUsersViewModelTest {

    private lateinit var viewModel: ManageUsersViewModel
    private lateinit var getUsersUseCase: GetUsersUseCase
    private lateinit var createUserUseCase: CreateUserUseCase
    private lateinit var updateUserUseCase: UpdateUserUseCase
    private lateinit var deleteUserUseCase: DeleteUserUseCase

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getUsersUseCase = mockk(relaxed = true)
        createUserUseCase = mockk(relaxed = true)
        updateUserUseCase = mockk(relaxed = true)
        deleteUserUseCase = mockk(relaxed = true)

        coEvery { getUsersUseCase() } returns flowOf(Result.success(emptyList()))

        viewModel = ManageUsersViewModel(
            getUsersUseCase = getUsersUseCase,
            createUserUseCase = createUserUseCase,
            updateUserUseCase = updateUserUseCase,
            deleteUserUseCase = deleteUserUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `state is updated with users when use case returns success`() = runTest {
        // Given
        val users = listOf(User("1", "test-id", "test-name", "waiter"))
        coEvery { getUsersUseCase() } returns flowOf(Result.success(users))

        // When
        viewModel.onRefresh()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            val successState = awaitItem()
            assertThat(successState.isRefreshing).isFalse()
            assertThat(successState.users).isEqualTo(users)
            assertThat(successState.error).isNull()
        }
    }

    @Test
    fun `state is updated with error when use case returns failure`() = runTest {
        val error = "Error"
        coEvery { getUsersUseCase() } returns flowOf(Result.failure(RuntimeException(error)))

        viewModel.onRefresh()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            val errorState = awaitItem()
            assertThat(errorState.isRefreshing).isFalse()
            assertThat(errorState.users).isEmpty()
            assertThat(errorState.error).isInstanceOf(UiText.DynamicString::class.java)
            assertThat((errorState.error as UiText.DynamicString).value).isEqualTo(error)
        }
    }

    @Test
    fun `onAddUserClick updates state to show add user dialog`() = runTest {
        // When
        viewModel.onAddUserClick()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.showAddUserDialog).isTrue()
        }
    }

    @Test
    fun `onDismissAddUserDialog updates state to hide add user dialog`() = runTest {
        // When
        viewModel.onDismissAddUserDialog()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.showAddUserDialog).isFalse()
        }
    }

    @Test
    fun `onEditUserClick updates state to show edit user dialog`() = runTest {
        // Given
        val user = User("1", "test-id", "test-name", "waiter")

        // When
        viewModel.onEditUserClick(user)

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.showEditUserDialog).isTrue()
            assertThat(state.selectedUser).isEqualTo(user)
        }
    }

    @Test
    fun `onDismissEditUserDialog updates state to hide edit user dialog`() = runTest {
        // When
        viewModel.onDismissEditUserDialog()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.showEditUserDialog).isFalse()
            assertThat(state.selectedUser).isNull()
        }
    }

    @Test
    fun `onDeleteUserClick updates state to show delete user dialog`() = runTest {
        // Given
        val user = User("1", "test-id", "test-name", "waiter")

        // When
        viewModel.onDeleteUserClick(user)

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.showDeleteUserDialog).isTrue()
            assertThat(state.selectedUser).isEqualTo(user)
        }
    }

    @Test
    fun `onDismissDeleteUserDialog updates state to hide delete user dialog`() = runTest {
        // When
        viewModel.onDismissDeleteUserDialog()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.showDeleteUserDialog).isFalse()
            assertThat(state.selectedUser).isNull()
        }
    }

    @Test
    fun `createUser calls createUserUseCase`() = runTest {
        // Given
        coEvery { createUserUseCase(any()) } returns Result.success(mockk())

        // When
        viewModel.createUser()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { createUserUseCase(any()) }
    }

    @Test
    fun `updateUser calls updateUserUseCase`() = runTest {
        // Given
        val user = User("1", "test-id", "test-name", "waiter")
        viewModel.onEditUserClick(user)
        coEvery { updateUserUseCase(any(), any()) } returns Result.success(mockk())

        // When
        viewModel.updateUser()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { updateUserUseCase(any(), any()) }
    }

    @Test
    fun `deleteUser calls deleteUserUseCase`() = runTest {
        // Given
        val user = User("1", "test-id", "test-name", "waiter")
        viewModel.onDeleteUserClick(user)
        coEvery { deleteUserUseCase(any()) } returns Result.success(Unit)

        // When
        viewModel.deleteUser()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { deleteUserUseCase(user.id) }
    }

    @Test
    fun `onRefresh emits snackbar event with mapped user exception`() = runTest {
        coEvery { getUsersUseCase() } returns flowOf(Result.failure(UserException.UserNotFound))

        viewModel.onRefresh()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.eventChannel.test {
            val event = awaitItem()
            assertThat(event).isInstanceOf(ManageUsersViewModel.UiEvent.ShowSnackbar::class.java)
            val message = (event as ManageUsersViewModel.UiEvent.ShowSnackbar).message
            assertThat(message).isInstanceOf(UiText.StringResource::class.java)
            assertThat((message as UiText.StringResource).resId).isEqualTo(R.string.error_user_not_found)
        }
    }

    @Test
    fun `createUser success emits success snackbar`() = runTest {
        coEvery { createUserUseCase(any()) } returns Result.success(User("1", "001", "Mario", "WAITER"))
        coEvery { getUsersUseCase() } returns flowOf(Result.success(emptyList()))

        viewModel.createUser()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.eventChannel.test {
            val event = awaitItem()
            assertThat(event).isInstanceOf(ManageUsersViewModel.UiEvent.ShowSnackbar::class.java)
            val message = (event as ManageUsersViewModel.UiEvent.ShowSnackbar).message
            assertThat(message).isInstanceOf(UiText.StringResource::class.java)
            assertThat((message as UiText.StringResource).resId).isEqualTo(R.string.user_created_success)
        }
    }
}