package dev.giuseppedarro.comanda.features.menu.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import dev.giuseppedarro.comanda.features.menu.presentation.CategoryScreen
import dev.giuseppedarro.comanda.features.menu.presentation.MenuScreen
import kotlinx.serialization.Serializable

@Serializable
object Menu
@Serializable
data class Category(val categoryName: String)

fun NavGraphBuilder.menuGraph(
    onNavigateToCategory: (categoryName: String) -> Unit,
    onNavigateBack: () -> Unit
) {
    composable<Menu> {
        MenuScreen(
            onNavigateToCategory = onNavigateToCategory,
            onNavigateBack = onNavigateBack
        )
    }

    composable<Category> { backStackEntry ->
        val args = backStackEntry.toRoute<Category>()
        CategoryScreen(
            onNavigateBack = onNavigateBack
        )
    }
}