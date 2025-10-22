package dev.giuseppedarro.comanda.features.tables.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.giuseppedarro.comanda.ui.theme.ComandaTheme

@Composable
fun TableDialog(
    onDismissRequest: () -> Unit,
    onConfirmClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var numberOfPeople by remember { mutableStateOf("") }
    val isConfirmEnabled = numberOfPeople.toIntOrNull()?.let { it > 0 } ?: false

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        title = { Text(text = "Open Table") },
        text = {
            Column {
                Text(text = "How many people are sitting at this table?")
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = numberOfPeople,
                    onValueChange = { newValue ->
                        // Allow only digits
                        if (newValue.all { it.isDigit() }) {
                            numberOfPeople = newValue
                        }
                    },
                    label = { Text("Number of people") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val count = numberOfPeople.toIntOrNull() ?: 0
                    onConfirmClick(count)
                },
                enabled = isConfirmEnabled
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}

@Preview
@Composable
fun TableDialogPreview() {
    ComandaTheme {
        TableDialog(
            onDismissRequest = {},
            onConfirmClick = {}
        )
    }
}
