package com.economy.splitpay.navigation

sealed class Routes(val route: String) {
    //main routes
    data object HomeScreen : Routes("home_screen")
    data object FriendsScreen : Routes("friends_screen")
    data object HistorialScreen : Routes("historial_screen")
    data object ProfileScreen : Routes("profile_screen")

    //home routes
    data object CreateGroupScreen : Routes("create_group_screen")
    data object AddMemberScreen : Routes("add_member_screen")
    data object PendingGroupsScreen : Routes("pending_group_screen/{groupId}")
    data object PaymentMethodScreen : Routes("payment_method_screen")

    //profile routes
    data object EditProfileScreen : Routes("edit_profile_screen")
    data object AccountScreen : Routes("account_screen")
    data object SettingsScreen : Routes("settings_screen")
    data object TermsScreen : Routes("terms_screen")

    //login routes
    data object LoginScreen : Routes("login")
    data object RegisterScreen : Routes("register")
}