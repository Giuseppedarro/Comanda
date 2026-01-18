package dev.giuseppedarro.comanda.features.tables.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.giuseppedarro.comanda.R
import dev.giuseppedarro.comanda.core.presentation.ComandaTopAppBar
import dev.giuseppedarro.comanda.features.tables.domain.model.Table
import dev.giuseppedarro.comanda.features.tables.presentation.components.TableCard
import dev.giuseppedarro.comanda.features.tables.presentation.components.TableDialog
import dev.giuseppedarro.comanda.ui.theme.ComandaTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TableOverviewScreen(
    onNavigateToOrder: (tableNumber: Int, numberOfPeople: Int) -> Unit,
    onNavigateToPrinters: () -> Unit,
    onNavigateToMenu: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onLogout: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: TableOverviewViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val event by viewModel.event.collectAsState()

    // Handle logout event
    LaunchedEffect(event) {
        when (event) {
            is TableOverviewEvent.LogoutSuccess -> {
                onLogout()
                viewModel.onEventConsumed()
            }
            null -> {}
        }
    }

    val onTableClick = { table: Table ->
        if (table.isOccupied) {
            // Pass 0 for occupied tables: the actual numberOfPeople will be fetched from the existing order
            onNavigateToOrder(table.number, 0)
        } else {
            viewModel.onTableClicked(table)
        }
    }

    if (uiState.isDialogShown) {
        TableDialog(
            onDismissRequest = viewModel::onDialogDismiss,
            onConfirmClick = { numberOfPeople ->
                uiState.selectedTable?.let {
                    onNavigateToOrder(it.number, numberOfPeople)
                }
                viewModel.onDialogDismiss()
            }
        )
    }

    TableOverviewContent(
        tables = uiState.tables,
        onTableClick = onTableClick,
        onNavigateToPrinters = onNavigateToPrinters,
        onNavigateToMenu = onNavigateToMenu,
        onNavigateToSettings = onNavigateToSettings,
        onLogout = viewModel::onLogout,
        isRefreshing = uiState.isRefreshing,
        onRefresh = viewModel::loadTables,
        onAddTableClick = viewModel::onAddTableClicked,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TableOverviewContent(
    tables: List<Table>,
    onTableClick: (Table) -> Unit,
    onNavigateToPrinters: () -> Unit,
    onNavigateToMenu: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onLogout: () -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onAddTableClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val pullRefreshState = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = onRefresh)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(280.dp),
                windowInsets = WindowInsets(0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.tertiaryContainer)
                ) {
                    Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
                    Column(modifier = Modifier.padding(16.dp)) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.background),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "GD",
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Giuseppe D\'Arrò",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
                Divider()
                NavigationDrawerItem(
                    label = { Text("Printers") },
                    icon = { Icon(Icons.Default.Print, contentDescription = "Printers") },
                    selected = false,
                    onClick = {
                        onNavigateToPrinters()
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Menu") },
                    icon = { Icon(Icons.Default.RestaurantMenu, contentDescription = "Menu") },
                    selected = false,
                    onClick = {
                        onNavigateToMenu()
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Settings (to be implemented)") },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    selected = false,
                    onClick = {
                        onNavigateToSettings()
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(label = { Text("Logout") },
                    icon = { Icon(Icons.Default.Logout, contentDescription = "Logout") },
                    selected = false,
                    onClick = {
                        onLogout()
                        scope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        Scaffold(
            modifier = modifier,
            topBar = {
                ComandaTopAppBar(
                    title = stringResource(id = R.string.app_name),
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onAddTableClick,
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add table")
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .pullRefresh(pullRefreshState)
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(tables) { table ->
                        TableCard(
                            tableNumber = table.number,
                            isOccupied = table.isOccupied,
                            onButtonClick = { onTableClick(table) }
                        )
                    }
                }

                PullRefreshIndicator(
                    refreshing = isRefreshing,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
fun TableOverviewScreenPreview() {
    ComandaTheme {
        TableOverviewContent(
            tables = List(10) { Table(number = it + 1, isOccupied = it % 2 == 0) },
            onTableClick = {},
            onNavigateToPrinters = {},
            onNavigateToMenu = {},
            onNavigateToSettings = {},
            onLogout = {},
            isRefreshing = false,
            onRefresh = {},
            onAddTableClick = {}
        )
    }
}
