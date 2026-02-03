package dev.giuseppedarro.comanda.features.printers.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import dev.giuseppedarro.comanda.R

@Composable
fun AddPrinterDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, address: String, port: Int) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var port by remember { mutableStateOf("9100") }
    var hasError by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(
                    text = stringResource(R.string.add_new_printer),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = MaterialTheme.typography.headlineSmall.fontWeight
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Name Field
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        hasError = false
                    },
                    label = { Text(stringResource(R.string.printer_name)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Print,
                            contentDescription = stringResource(R.string.printer)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    isError = hasError && name.isBlank(),
                    supportingText = if (hasError && name.isBlank()) {
                        { Text(stringResource(R.string.name_is_required)) }
                    } else null
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Address Field
                OutlinedTextField(
                    value = address,
                    onValueChange = {
                        address = it
                        hasError = false
                    },
                    label = { Text(stringResource(R.string.ip_address)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Wifi,
                            contentDescription = stringResource(R.string.ip_address)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    isError = hasError && address.isBlank(),
                    supportingText = if (hasError && address.isBlank()) {
                        { Text(stringResource(R.string.address_is_required)) }
                    } else null
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Port Field
                OutlinedTextField(
                    value = port,
                    onValueChange = {
                        port = it.filter { it.isDigit() }
                        hasError = false
                    },
                    label = { Text(stringResource(R.string.port)) },
                    modifier = Modifier.fillMaxWidth(),
                    isError = hasError && (port.isBlank() || port.toIntOrNull() == null),
                    supportingText = if (hasError) {
                        { Text(stringResource(R.string.invalid_port_number)) }
                    } else null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(stringResource(R.string.cancel))
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            if (name.isBlank() || address.isBlank() || port.isBlank() || port.toIntOrNull() == null) {
                                hasError = true
                                return@Button
                            }
                            onConfirm(name, address, port.toInt())
                        }
                    ) {
                        Text(stringResource(R.string.add_printer))
                    }
                }
            }
        }
    }
}