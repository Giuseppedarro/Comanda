package dev.giuseppedarro.comanda.features.menu.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun MenuItemDialog(
    itemName: String,
    itemDescription: String,
    itemPrice: String,
    itemAvailable: Boolean,
    onItemNameChange: (String) -> Unit,
    onItemDescriptionChange: (String) -> Unit,
    onItemPriceChange: (String) -> Unit,
    onItemAvailableChange: (Boolean) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    isEditing: Boolean
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (isEditing) "Edit Menu Item" else "Add Menu Item")
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = itemName,
                    onValueChange = onItemNameChange,
                    label = { Text("Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = itemDescription,
                    onValueChange = onItemDescriptionChange,
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = itemPrice,
                    onValueChange = onItemPriceChange,
                    label = { Text("Price") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Available", modifier = Modifier.weight(1f))
                    Checkbox(
                        checked = itemAvailable,
                        onCheckedChange = onItemAvailableChange
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                enabled = itemName.isNotBlank() && itemPrice.isNotBlank()
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}