package dev.giuseppedarro.comanda.core.navigation

sealed class FeatureGraph(val route: String) {
    object Login : FeatureGraph("login_graph")
    object Tables : FeatureGraph("tables_graph")
    object Orders : FeatureGraph("orders_graph")
}
