package dev.giuseppedarro.comanda.core.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object TableOverview : Screen("table_overview")
    object MenuOrder : Screen("menu_order")
}
