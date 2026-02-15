package dev.giuseppedarro.comanda.features.menu.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import dev.giuseppedarro.comanda.navigation.Category
import dev.giuseppedarro.comanda.navigation.Menu
import dev.giuseppedarro.comanda.features.menu.presentation.CategoryScreen
import dev.giuseppedarro.comanda.features.menu.presentation.MenuScreen

fun NavGraphBuilder.menuGraph(navController: NavController) {
    composable<Menu> {
        MenuScreen(
            onNavigateToCategory = { categoryName ->
                navController.navigate(Category(categoryName))
            },
            onNavigateBack = {
                navController.popBackStack()
            }
        )
    }

    composable<Category> { backStackEntry ->
        val args = backStackEntry.toRoute<Category>()
        CategoryScreen(
            onNavigateBack = {
                navController.popBackStack()
            }
        )
    }
}