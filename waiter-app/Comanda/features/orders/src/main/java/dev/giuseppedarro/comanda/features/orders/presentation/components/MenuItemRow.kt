package dev.giuseppedarro.comanda.features.orders.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.giuseppedarro.comanda.features.orders.R
import dev.giuseppedarro.comanda.core.ui.theme.ComandaTheme

@Composable
fun MenuItemRow(
    itemName: String,
    itemPrice: String,
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = itemName,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = itemPrice,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            IconButton(onClick = { onQuantityChange(quantity - 1) }) {
                Icon(Icons.Default.RemoveCircle, contentDescription = stringResource(R.string.remove))
            }
            Text(text = quantity.toString())
            IconButton(onClick = { onQuantityChange(quantity + 1) }) {
                Icon(Icons.Default.AddCircle, contentDescription = stringResource(R.string.add))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MenuItemRowPreview() {
    ComandaTheme {
        MenuItemRow(
            itemName = "Gourmet Burger",
            itemPrice = "€12.99",
            quantity = 1,
            onQuantityChange = {}
        )
    }
}
