package dev.giuseppedarro.comanda.features.menu.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dev.giuseppedarro.comanda.features.menu.domain.model.MenuItem

@Composable
internal fun EditMenuItemDialog(
    menuItem: MenuItem,
    onDismiss: () -> Unit,
    onConfirm: (name: String, description: String, price: String, isAvailable: Boolean) -> Unit
) {
    var itemName by remember { mutableStateOf(menuItem.name) }
    var itemDescription by remember { mutableStateOf(menuItem.description) }
    var itemPrice by remember {
        mutableStateOf(menuItem.price.let { "${it / 100}.${(it % 100).toString().padStart(2, '0')}" })
    }
    var itemAvailable by remember { mutableStateOf(menuItem.isAvailable) }

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
        isEditing = true
    )
}