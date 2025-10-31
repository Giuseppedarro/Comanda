package dev.giuseppedarro.comanda.features.orders.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.giuseppedarro.comanda.ui.theme.ComandaTheme

@Composable
fun MenuItemRow(
    itemName: String,
    itemPrice: String,
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = itemName, style = MaterialTheme.typography.bodyLarge)
            Text(text = itemPrice, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { onQuantityChange(quantity - 1) }) {
                Icon(Icons.Default.RemoveCircle, contentDescription = "Remove")
            }
            Text(text = quantity.toString(), modifier = Modifier.padding(horizontal = 8.dp))
            IconButton(onClick = { onQuantityChange(quantity + 1) }) {
                Icon(Icons.Default.AddCircle, contentDescription = "Add")
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
            itemPrice = "$12.99",
            quantity = 1,
            onQuantityChange = {}
        )
    }
}
