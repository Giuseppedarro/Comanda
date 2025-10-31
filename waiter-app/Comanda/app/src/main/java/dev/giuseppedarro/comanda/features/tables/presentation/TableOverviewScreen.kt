package dev.giuseppedarro.comanda.features.tables.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

@Composable
fun TableOverviewScreen(
    onNavigateToOrder: (tableNumber: Int, numberOfPeople: Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TableOverviewViewModel = koinViewModel()
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
        uiState = uiState,
        onTableClick = onTableClick,
        onSettingsClick = { /* TODO: Handle settings click */ },
        onRefresh = viewModel::refresh,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TableOverviewContent(
    uiState: TableOverviewUiState,
    onTableClick: (Table) -> Unit,
    onSettingsClick: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
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
        }
    ) { innerPadding ->
        PullToRefreshBox(
            modifier = Modifier.padding(innerPadding),
            isRefreshing = uiState.isLoading,
            onRefresh = onRefresh
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.tables) { table ->
                    TableCard(
                        tableNumber = table.number,
                        isOccupied = table.isOccupied,
                        onButtonClick = { onTableClick(table) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TableOverviewScreenPreview() {
    ComandaTheme {
        TableOverviewContent(
            uiState = TableOverviewUiState(
                tables = List(20) { Table(number = it + 1, isOccupied = it % 2 == 0) }
            ),
            onTableClick = {},
            onSettingsClick = {},
            onRefresh = {}
        )
    }
}
