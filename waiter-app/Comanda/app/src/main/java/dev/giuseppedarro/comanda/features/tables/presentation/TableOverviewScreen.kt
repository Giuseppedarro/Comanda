package dev.giuseppedarro.comanda.features.tables.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.giuseppedarro.comanda.R
import dev.giuseppedarro.comanda.core.presentation.ComandaBottomBar
import dev.giuseppedarro.comanda.core.presentation.ComandaTopAppBar
import dev.giuseppedarro.comanda.features.tables.presentation.components.TableCard
import dev.giuseppedarro.comanda.features.tables.presentation.components.TableDialog
import dev.giuseppedarro.comanda.ui.theme.ComandaTheme

data class Table(val number: Int, val isOccupied: Boolean)

@Composable
fun TableOverviewScreen(
    onTableClick: (Table) -> Unit, // This will be used for occupied tables
    onNewOrderClick: (tableNumber: Int, numberOfPeople: Int) -> Unit, // For new tables
    modifier: Modifier = Modifier
) {
    // In the future, we will connect this to a ViewModel
    val tables = List(10) { Table(number = it + 1, isOccupied = it % 2 == 0) }

    var showDialog by remember { mutableStateOf(false) }
    var selectedTable by remember { mutableStateOf<Table?>(null) }

    if (showDialog && selectedTable != null) {
        TableDialog(
            onDismissRequest = { showDialog = false },
            onConfirmClick = { numberOfPeople ->
                showDialog = false
                onNewOrderClick(selectedTable!!.number, numberOfPeople)
            }
        )
    }

    TableOverviewContent(
        tables = tables,
        onTableClick = {
            if (it.isOccupied) {
                onTableClick(it)
            } else {
                selectedTable = it
                showDialog = true
            }
        },
        onSettingsClick = { /* TODO: Handle settings click */ },
        onHomeClick = { /* TODO: Handle home click */ },
        onProfileClick = { /* TODO: Handle profile click */ },
        modifier = modifier
    )
}

@Composable
fun TableOverviewContent(
    tables: List<Table>,
    onTableClick: (Table) -> Unit,
    onSettingsClick: () -> Unit,
    onHomeClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            ComandaTopAppBar(
                title = stringResource(id = R.string.app_name,),
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        bottomBar = {
            ComandaBottomBar(
                onHomeClick = onHomeClick,
                onProfileClick = onProfileClick
            )
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(innerPadding).padding(8.dp),
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
    }
}

@Preview(showBackground = true)
@Composable
fun TableOverviewScreenPreview() {
    ComandaTheme {
        TableOverviewScreen(
            onTableClick = {},
            onNewOrderClick = { _, _ -> }
        )
    }
}
