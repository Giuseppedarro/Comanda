package dev.giuseppedarro.comanda.features.tables.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.giuseppedarro.comanda.ui.theme.ComandaTheme

@Composable
fun TableCard(
    tableNumber: Int,
    isOccupied: Boolean,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "Table $tableNumber")
            Text(text = if (isOccupied) "Occupied" else "Available")
            Button(onClick = onButtonClick) {
                Text(text = if (isOccupied) "Modify Order" else "Take Order")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TableCardOccupiedPreview() {
    ComandaTheme {
        TableCard(tableNumber = 1, isOccupied = true, onButtonClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun TableCardAvailablePreview() {
    ComandaTheme {
        TableCard(tableNumber = 2, isOccupied = false, onButtonClick = {})
    }
}
