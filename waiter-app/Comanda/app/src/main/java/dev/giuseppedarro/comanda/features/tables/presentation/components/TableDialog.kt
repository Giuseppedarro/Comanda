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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.giuseppedarro.comanda.R
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
        title = { Text(text = stringResource(R.string.open_table)) },
        text = {
            Column {
                Text(text = stringResource(R.string.how_many_people_are_sitting_at_this_table))
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = numberOfPeople,
                    onValueChange = { newValue ->
                        // Allow only digits
                        if (newValue.all { it.isDigit() }) {
                            numberOfPeople = newValue
                        }
                    },
                    label = { Text(stringResource(R.string.number_of_people)) },
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
                Text(stringResource(R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(R.string.cancel))
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
