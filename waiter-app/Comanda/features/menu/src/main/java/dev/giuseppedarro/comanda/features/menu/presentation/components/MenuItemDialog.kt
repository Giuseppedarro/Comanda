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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.giuseppedarro.comanda.features.menu.R

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
            Text(if (isEditing) stringResource(R.string.edit_menu_item) else stringResource(R.string.add_menu_item))
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = itemName,
                    onValueChange = onItemNameChange,
                    label = { Text(stringResource(R.string.name)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = itemDescription,
                    onValueChange = onItemDescriptionChange,
                    label = { Text(stringResource(R.string.description)) },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = itemPrice,
                    onValueChange = onItemPriceChange,
                    label = { Text(stringResource(R.string.price)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(R.string.available), modifier = Modifier.weight(1f))
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
                Text(stringResource(R.string.save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}