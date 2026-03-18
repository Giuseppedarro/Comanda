package dev.giuseppedarro.comanda.features.tables.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import dev.giuseppedarro.comanda.features.tables.R

@Composable
fun AddTableDialog(
    onDismissRequest: () -> Unit,
    onConfirmClick: (Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    var isIncremental by remember { mutableStateOf(true) }
    var tableNumber by remember { mutableStateOf("") }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        title = { Text(text = stringResource(R.string.add_new_table)) },
        text = {
            Column(Modifier.selectableGroup()) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .selectable(
                            selected = isIncremental,
                            onClick = { isIncremental = true },
                            role = Role.RadioButton
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = isIncremental,
                        onClick = null // null recommended for accessibility with screen readers
                    )
                    Text(
                        text = stringResource(R.string.incremental_add),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .selectable(
                            selected = !isIncremental,
                            onClick = { isIncremental = false },
                            role = Role.RadioButton
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = !isIncremental,
                        onClick = null
                    )
                    Text(
                        text = stringResource(R.string.custom_add),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }

                if (!isIncremental) {
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = tableNumber,
                        onValueChange = { if (it.all { char -> char.isDigit() }) tableNumber = it },
                        label = { Text(stringResource(R.string.table_number_hint)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (isIncremental) {
                        onConfirmClick(null)
                    } else {
                        tableNumber.toIntOrNull()?.let { onConfirmClick(it) }
                    }
                },
                enabled = isIncremental || tableNumber.toIntOrNull() != null
            ) {
                Text(stringResource(R.string.add))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}
