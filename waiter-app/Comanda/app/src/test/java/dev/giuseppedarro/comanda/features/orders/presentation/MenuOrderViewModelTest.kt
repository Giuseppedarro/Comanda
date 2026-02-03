package dev.giuseppedarro.comanda.features.orders.presentation

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.giuseppedarro.comanda.core.utils.Result
import dev.giuseppedarro.comanda.features.orders.domain.model.MenuItem
import dev.giuseppedarro.comanda.features.orders.domain.model.Order
import dev.giuseppedarro.comanda.features.orders.domain.model.OrderItem
import dev.giuseppedarro.comanda.features.orders.domain.model.OrderStatus
import dev.giuseppedarro.comanda.features.orders.domain.use_case.GetMenuUseCase
import dev.giuseppedarro.comanda.features.orders.domain.use_case.GetOrdersForTableUseCase
import dev.giuseppedarro.comanda.features.orders.domain.use_case.PrintBillUseCase
import dev.giuseppedarro.comanda.features.orders.domain.use_case.SubmitOrderUseCase
import io.mockk.coEvery
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
class MenuOrderViewModelTest {

    private lateinit var getMenu: GetMenuUseCase
    private lateinit var getOrdersForTable: GetOrdersForTableUseCase
    private lateinit var submitOrder: SubmitOrderUseCase
    private lateinit var printBill: PrintBillUseCase
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getMenu = mockk()
        getOrdersForTable = mockk()
        submitOrder = mockk()
        printBill = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init loads menu successfully`() = runTest {
        // Given
        coEvery { getMenu() } returns flowOf(Result.Success(emptyList()))
        coEvery { getOrdersForTable(any()) } returns flowOf(Result.Success(null))

        // When
        val viewModel = MenuOrderViewModel(SavedStateHandle(), getMenu, getOrdersForTable, submitOrder, printBill)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.isLoading).isFalse()
            assertThat(state.menuCategories).isEmpty()
        }
    }

    @Test
    fun `init fails to load menu`() = runTest {
        // Given
        coEvery { getMenu() } returns flowOf(Result.Error("Failed to load menu"))
        coEvery { getOrdersForTable(any()) } returns flowOf(Result.Success(null))

        // When
        val viewModel = MenuOrderViewModel(SavedStateHandle(), getMenu, getOrdersForTable, submitOrder, printBill)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.error).isEqualTo("Failed to load menu")
        }
    }

    @Test
    fun `init loads existing order successfully`() = runTest {
        // Given
        val existingItem = OrderItem(MenuItem("1", "Pizza", 1000), 2)
        val existingOrder = Order(1, 4, OrderStatus.OPEN, listOf(existingItem), "")
        val savedStateHandle = SavedStateHandle(mapOf("tableNumber" to 1))
        coEvery { getMenu() } returns flowOf(Result.Success(emptyList()))
        coEvery { getOrdersForTable(1) } returns flowOf(Result.Success(existingOrder))

        // When
        val viewModel = MenuOrderViewModel(savedStateHandle, getMenu, getOrdersForTable, submitOrder, printBill)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.orderItems).hasSize(1)
            assertThat(state.orderItems.first().quantity).isEqualTo(2)
        }
    }

    @Test
    fun `onMenuItemAdded adds new item`() = runTest {
        coEvery { getMenu() } returns flowOf(Result.Success(emptyList()))
        coEvery { getOrdersForTable(any()) } returns flowOf(Result.Success(null))
        val viewModel = MenuOrderViewModel(SavedStateHandle(), getMenu, getOrdersForTable, submitOrder, printBill)
        val menuItem = MenuItem(id = "1", name = "Pizza", price = 1000)

        viewModel.onMenuItemAdded(menuItem)

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.orderItems).hasSize(1)
            assertThat(state.orderItems.first().menuItem).isEqualTo(menuItem)
        }
    }

    @Test
    fun `onMenuItemAdded increments quantity of existing item`() = runTest {
        coEvery { getMenu() } returns flowOf(Result.Success(emptyList()))
        coEvery { getOrdersForTable(any()) } returns flowOf(Result.Success(null))
        val viewModel = MenuOrderViewModel(SavedStateHandle(), getMenu, getOrdersForTable, submitOrder, printBill)
        val menuItem = MenuItem(id = "1", name = "Pizza", price = 1000)

        viewModel.onMenuItemAdded(menuItem) // Add once
        viewModel.onMenuItemAdded(menuItem) // Add again

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.orderItems).hasSize(1)
            assertThat(state.orderItems.first().quantity).isEqualTo(2)
        }
    }

    @Test
    fun `onQuantityChange updates quantity`() = runTest {
        coEvery { getMenu() } returns flowOf(Result.Success(emptyList()))
        coEvery { getOrdersForTable(any()) } returns flowOf(Result.Success(null))
        val viewModel = MenuOrderViewModel(SavedStateHandle(), getMenu, getOrdersForTable, submitOrder, printBill)
        val menuItem = MenuItem(id = "1", name = "Pizza", price = 1000)
        viewModel.onMenuItemAdded(menuItem)

        val itemToChange = viewModel.uiState.value.orderItems.first()
        viewModel.onQuantityChange(itemToChange, 5)

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.orderItems.first().quantity).isEqualTo(5)
        }
    }

    @Test
    fun `onQuantityChange removes item if quantity is zero`() = runTest {
        coEvery { getMenu() } returns flowOf(Result.Success(emptyList()))
        coEvery { getOrdersForTable(any()) } returns flowOf(Result.Success(null))
        val viewModel = MenuOrderViewModel(SavedStateHandle(), getMenu, getOrdersForTable, submitOrder, printBill)
        val menuItem = MenuItem(id = "1", name = "Pizza", price = 1000)
        viewModel.onMenuItemAdded(menuItem)

        val itemToChange = viewModel.uiState.value.orderItems.first()
        viewModel.onQuantityChange(itemToChange, 0)

        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.orderItems).isEmpty()
        }
    }

    @Test
    fun `onSendOrder succeeds`() = runTest {
        val savedStateHandle = SavedStateHandle(mapOf("tableNumber" to 1, "numberOfPeople" to 4))
        coEvery { getMenu() } returns flowOf(Result.Success(emptyList()))
        coEvery { getOrdersForTable(any()) } returns flowOf(Result.Success(null))
        coEvery { submitOrder(any(), any(), any()) } returns Result.Success(Unit)
        val viewModel = MenuOrderViewModel(savedStateHandle, getMenu, getOrdersForTable, submitOrder, printBill)
        val menuItem = MenuItem(id = "1", name = "Pizza", price = 1000)
        viewModel.onMenuItemAdded(menuItem)

        var successCalled = false
        viewModel.onSendOrder(1, 4, onSuccess = { successCalled = true }, onError = {})
        testDispatcher.scheduler.advanceUntilIdle()

        assertThat(successCalled).isTrue()
    }

    @Test
    fun `onSendOrder fails`() = runTest {
        val savedStateHandle = SavedStateHandle(mapOf("tableNumber" to 1, "numberOfPeople" to 4))
        coEvery { getMenu() } returns flowOf(Result.Success(emptyList()))
        coEvery { getOrdersForTable(any()) } returns flowOf(Result.Success(null))
        coEvery { submitOrder(any(), any(), any()) } returns Result.Error("Network Error")
        val viewModel = MenuOrderViewModel(savedStateHandle, getMenu, getOrdersForTable, submitOrder, printBill)
        val menuItem = MenuItem(id = "1", name = "Pizza", price = 1000)
        viewModel.onMenuItemAdded(menuItem)

        var errorMsg = ""
        viewModel.onSendOrder(1, 4, onSuccess = {}, onError = { errorMsg = it })
        testDispatcher.scheduler.advanceUntilIdle()

        assertThat(errorMsg).isEqualTo("Network Error")
    }
}