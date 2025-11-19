package dev.giuseppedarro.comanda.features.tables.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.giuseppedarro.comanda.R
import dev.giuseppedarro.comanda.core.presentation.ComandaTopAppBar
import dev.giuseppedarro.comanda.features.tables.domain.model.Table
import dev.giuseppedarro.comanda.features.tables.presentation.components.TableCard
import dev.giuseppedarro.comanda.features.tables.presentation.components.TableDialog
import dev.giuseppedarro.comanda.ui.theme.ComandaTheme
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TableOverviewScreen(
    onNavigateToOrder: (tableNumber: Int, numberOfPeople: Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TableOverviewViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    val onTableClick = { table: Table ->
        if (table.isOccupied) {
            onNavigateToOrder(table.number, 1)
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
        onSettingsClick = { /* TODO: Handle settings click */ },
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
    onSettingsClick: () -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onAddTableClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val pullRefreshState = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = onRefresh)

    Scaffold(
        modifier = modifier,
        topBar = {
            ComandaTopAppBar(
                title = stringResource(id = R.string.app_name),
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
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

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
fun TableOverviewScreenPreview() {
    ComandaTheme {
        TableOverviewContent(
            tables = List(10) { Table(number = it + 1, isOccupied = it % 2 == 0) },
            onTableClick = {},
            onSettingsClick = {},
            isRefreshing = false,
            onRefresh = {},
            onAddTableClick = {}
        )
    }
}
