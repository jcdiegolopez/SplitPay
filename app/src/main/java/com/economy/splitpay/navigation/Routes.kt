package com.economy.splitpay.navigation

sealed class Routes(val route: String) {
    data object HomeScreen : Routes("home_screen")
    data object FriendsScreen : Routes("friends_screen")
    data object HistorialScreen : Routes("historial_screen")
    data object ProfileScreen : Routes("profile_screen")
}