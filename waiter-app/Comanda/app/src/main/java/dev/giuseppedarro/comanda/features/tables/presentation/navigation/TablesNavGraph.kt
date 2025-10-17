package dev.giuseppedarro.comanda.features.tables.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.giuseppedarro.comanda.core.navigation.FeatureGraph
import dev.giuseppedarro.comanda.features.tables.presentation.TableOverviewScreen

fun NavGraphBuilder.tablesGraph(navController: NavController) {
    composable(FeatureGraph.Tables.route) {
        TableOverviewScreen(
            onTableClick = {
                // Navigate to the nested orders graph
                navController.navigate(FeatureGraph.Orders.route)
            }
        )
    }
}
