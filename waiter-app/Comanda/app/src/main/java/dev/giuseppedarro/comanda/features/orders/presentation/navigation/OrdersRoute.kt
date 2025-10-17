package dev.giuseppedarro.comanda.features.orders.presentation.navigation

sealed class OrdersRoute(val route: String) {
    object MenuOrder : OrdersRoute("menu_order")
    // object OrderDetail : OrdersRoute("order_detail")
    // object OrderSummary : OrdersRoute("order_summary")
}
