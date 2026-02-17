package dev.giuseppedarro.comanda.features.tables.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.giuseppedarro.comanda.features.tables.R
import dev.giuseppedarro.comanda.core.presentation.ComandaTopAppBar
import dev.giuseppedarro.comanda.features.tables.domain.model.Table
import dev.giuseppedarro.comanda.features.tables.presentation.components.AppDrawer
import dev.giuseppedarro.comanda.features.tables.presentation.components.TableCard
import dev.giuseppedarro.comanda.features.tables.presentation.components.TableDialog
import dev.giuseppedarro.comanda.core.ui.theme.ComandaTheme
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
        uiState = uiState,
        onTableClick = onTableClick,
        onNavigateToPrinters = onNavigateToPrinters,
        onNavigateToMenu = onNavigateToMenu,
        onNavigateToSettings = onNavigateToSettings,
        onLogout = viewModel::onLogout,
        onRefresh = viewModel::loadTables,
        onAddTableClick = viewModel::onAddTableClicked,
        onFilterChanged = viewModel::onFilterChanged,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TableOverviewContent(
    uiState: TableOverviewUiState,
    onTableClick: (Table) -> Unit,
    onNavigateToPrinters: () -> Unit,
    onNavigateToMenu: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onLogout: () -> Unit,
    onRefresh: () -> Unit,
    onAddTableClick: () -> Unit,
    onFilterChanged: (TableFilter) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pullRefreshState = rememberPullRefreshState(refreshing = uiState.isRefreshing, onRefresh = onRefresh)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val gridState = rememberLazyGridState()
    val fabIsVisible by remember {
        derivedStateOf {
            gridState.firstVisibleItemIndex == 0
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                onNavigateToPrinters = onNavigateToPrinters,
                onNavigateToMenu = onNavigateToMenu,
                onNavigateToSettings = onNavigateToSettings,
                onLogout = onLogout,
                onCloseDrawer = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            modifier = modifier,
            topBar = {
                ComandaTopAppBar(
                    title = stringResource(id = R.string.app_name),
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = stringResource(R.string.menu))
                        }
                    }
                )
            },
            floatingActionButton = {
                AnimatedVisibility(
                    visible = fabIsVisible,
                    enter = slideInVertically(initialOffsetY = { it * 2 }),
                    exit = slideOutVertically(targetOffsetY = { it * 2 })
                ) {
                    FloatingActionButton(
                        onClick = onAddTableClick,
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ) {
                        Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_table))
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                FilterOptions(
                    selectedFilter = uiState.filter,
                    onFilterChanged = onFilterChanged,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pullRefresh(pullRefreshState)
                ) {
                    LazyVerticalGrid(
                        state = gridState,
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.filteredTables) { table ->
                            TableCard(
                                tableNumber = table.number,
                                isOccupied = table.isOccupied,
                                onButtonClick = { onTableClick(table) }
                            )
                        }
                    }

                    PullRefreshIndicator(
                        refreshing = uiState.isRefreshing,
                        state = pullRefreshState,
                        modifier = Modifier.align(Alignment.TopCenter)
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterOptions(
    selectedFilter: TableFilter,
    onFilterChanged: (TableFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            selected = selectedFilter == TableFilter.OCCUPIED,
            onClick = { onFilterChanged(TableFilter.OCCUPIED) },
            label = { Text(stringResource(R.string.occupied)) },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.primary,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
            )
        )
        FilterChip(
            selected = selectedFilter == TableFilter.AVAILABLE,
            onClick = { onFilterChanged(TableFilter.AVAILABLE) },
            label = { Text(stringResource(R.string.available)) },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.primary,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
            )
        )
        FilterChip(
            selected = selectedFilter == TableFilter.ALL,
            onClick = { onFilterChanged(TableFilter.ALL) },
            label = { Text(stringResource(R.string.all)) },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.primary,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
            )
        )
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun TableOverviewScreenPreview() {
    ComandaTheme {
        val uiState = TableOverviewUiState(
            tables = List(10) { Table(number = it + 1, isOccupied = it % 2 == 0) },
        )
        TableOverviewContent(
            uiState = uiState,
            onTableClick = {},
            onNavigateToPrinters = {},
            onNavigateToMenu = {},
            onNavigateToSettings = {},
            onLogout = {},
            onRefresh = {},
            onAddTableClick = {},
            onFilterChanged = {}
        )
    }
}
