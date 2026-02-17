package dev.giuseppedarro.comanda.features.tables.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.giuseppedarro.comanda.features.tables.R
import dev.giuseppedarro.comanda.core.ui.theme.ComandaTheme
import dev.giuseppedarro.comanda.core.ui.theme.Green
import dev.giuseppedarro.comanda.core.ui.theme.Orange

@Composable
fun TableCard(
    tableNumber: Int,
    isOccupied: Boolean,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Table $tableNumber")
                Icon(
                    painter = if (isOccupied) painterResource(R.drawable.ic_occupied)
                    else painterResource(R.drawable.ic_available),
                    contentDescription = null,
                    tint = if (isOccupied) Orange else Green
                )
            }

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
