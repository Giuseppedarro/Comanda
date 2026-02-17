package dev.giuseppedarro.comanda.features.menu.presentation

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.giuseppedarro.comanda.features.menu.domain.model.MenuCategory
import dev.giuseppedarro.comanda.features.menu.domain.model.MenuItem
import dev.giuseppedarro.comanda.features.menu.domain.usecase.AddMenuItemUseCase
import dev.giuseppedarro.comanda.features.menu.domain.usecase.DeleteMenuItemUseCase
import dev.giuseppedarro.comanda.features.menu.domain.usecase.GetMenuUseCase
import dev.giuseppedarro.comanda.features.menu.domain.usecase.UpdateMenuItemUseCase
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
class CategoryViewModelTest {

    private lateinit var viewModel: CategoryViewModel
    private lateinit var getMenuUseCase: GetMenuUseCase
    private lateinit var addMenuItemUseCase: AddMenuItemUseCase
    private lateinit var updateMenuItemUseCase: UpdateMenuItemUseCase
    private lateinit var deleteMenuItemUseCase: DeleteMenuItemUseCase
    private lateinit var savedStateHandle: SavedStateHandle

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getMenuUseCase = mockk(relaxed = true)
        addMenuItemUseCase = mockk(relaxed = true)
        updateMenuItemUseCase = mockk(relaxed = true)
        deleteMenuItemUseCase = mockk(relaxed = true)
        savedStateHandle = SavedStateHandle().apply {
            set("categoryName", "Pizzas")
        }
        viewModel = CategoryViewModel(
            savedStateHandle = savedStateHandle,
            getMenuUseCase = getMenuUseCase,
            addMenuItemUseCase = addMenuItemUseCase,
            updateMenuItemUseCase = updateMenuItemUseCase,
            deleteMenuItemUseCase = deleteMenuItemUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `state is updated with category items when use case returns success`() = runTest {
        // Given
        val menu = listOf(MenuCategory("1", "Pizzas", emptyList()))
        coEvery { getMenuUseCase() } returns flowOf(Result.success(menu))

        // When
        viewModel.loadCategory()

        // Then
        viewModel.uiState.test {
            val loadingState = awaitItem()
            assertThat(loadingState.isLoading).isTrue()

            val successState = awaitItem()
            assertThat(successState.isLoading).isFalse()
            assertThat(successState.items).isEmpty()
            assertThat(successState.error).isNull()
        }
    }

    @Test
    fun `state is updated with error when use case returns failure`() = runTest {
        // Given
        val error = "Error"
        coEvery { getMenuUseCase() } returns flowOf(Result.failure(RuntimeException(error)))

        // When
        viewModel.loadCategory()

        // Then
        viewModel.uiState.test {
            val loadingState = awaitItem()
            assertThat(loadingState.isLoading).isTrue()

            val errorState = awaitItem()
            assertThat(errorState.isLoading).isFalse()
            assertThat(errorState.items).isEmpty()
            assertThat(errorState.error).isEqualTo(error)
        }
    }

    @Test
    fun `onAddItemClick updates state to show dialog`() = runTest {
        // When
        viewModel.onAddItemClick()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.isDialogShown).isTrue()
            assertThat(state.selectedItem).isNull()
        }
    }

    @Test
    fun `onEditItemClick updates state to show dialog with selected item`() = runTest {
        // Given
        val menuItem = MenuItem("1", "Margherita", "", 0.0)

        // When
        viewModel.onEditItemClick(menuItem)

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.isDialogShown).isTrue()
            assertThat(state.selectedItem).isEqualTo(menuItem)
        }
    }

    @Test
    fun `onDialogDismiss updates state to hide dialog`() = runTest {
        // When
        viewModel.onDialogDismiss()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.isDialogShown).isFalse()
            assertThat(state.selectedItem).isNull()
        }
    }

    @Test
    fun `onSaveItem calls addMenuItem when selectedItem is null`() = runTest {
        // Given
        coEvery { addMenuItemUseCase(any(), any()) } returns Result.success(Unit)

        // When
        viewModel.onSaveItem("Margherita", "", "12.34")

        // Then
        coVerify { addMenuItemUseCase(any(), any()) }
    }

    @Test
    fun `onSaveItem calls updateMenuItem when selectedItem is not null`() = runTest {
        // Given
        val menuItem = MenuItem("1", "Margherita", "", 0.0)
        viewModel.onEditItemClick(menuItem)
        coEvery { updateMenuItemUseCase(any()) } returns Result.success(Unit)

        // When
        viewModel.onSaveItem("Margherita", "", "12.34")

        // Then
        coVerify { updateMenuItemUseCase(any()) }
    }

    @Test
    fun `onDeleteItemClick calls deleteMenuItem`() = runTest {
        // Given
        val menuItem = MenuItem("1", "Margherita", "", 0.0)
        coEvery { deleteMenuItemUseCase(any()) } returns Result.success(Unit)

        // When
        viewModel.onDeleteItemClick(menuItem)

        // Then
        coVerify { deleteMenuItemUseCase(menuItem.id) }
    }
}