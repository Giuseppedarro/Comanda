package dev.giuseppedarro.comanda.features.menu.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
internal fun AddMenuItemDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, description: String, price: String, isAvailable: Boolean) -> Unit
) {
    var itemName by remember { mutableStateOf("") }
    var itemDescription by remember { mutableStateOf("") }
    var itemPrice by remember { mutableStateOf("") }
    var itemAvailable by remember { mutableStateOf(true) }

    MenuItemDialog(
        itemName = itemName,
        itemDescription = itemDescription,
        itemPrice = itemPrice,
        itemAvailable = itemAvailable,
        onItemNameChange = { itemName = it },
        onItemDescriptionChange = { itemDescription = it },
        onItemPriceChange = { itemPrice = it },
        onItemAvailableChange = { itemAvailable = it },
        onDismiss = onDismiss,
        onConfirm = {
            onConfirm(itemName, itemDescription, itemPrice, itemAvailable)
        },
        isEditing = false
    )
}