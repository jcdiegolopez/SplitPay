package com.economy.splitpay.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.economy.splitpay.ui.screens.friends.FriendsScreen
import com.economy.splitpay.ui.screens.history.HistorialScreen
import com.economy.splitpay.ui.screens.home.AddMemberScreen
import com.economy.splitpay.ui.screens.home.CreateGroupScreen
import com.economy.splitpay.ui.screens.home.HomeScreen
import com.economy.splitpay.ui.screens.home.PaymentMethodScreen
import com.economy.splitpay.ui.screens.home.PendingGroupScreen
import com.economy.splitpay.ui.screens.login.LoginScreen
import com.economy.splitpay.ui.screens.profile.EditProfileScreen
import com.economy.splitpay.ui.screens.profile.ProfileScreen
import com.economy.splitpay.ui.screens.profile.AccountScreen
import com.economy.splitpay.ui.screens.profile.TermsScreen
import com.economy.splitpay.ui.screens.register.RegisterScreen



@Composable
fun AppNavigation(navController: NavHostController, innerPadding : PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = Routes.LoginScreen.route,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(Routes.HomeScreen.route) {
            HomeScreen(navController)
        }
        composable(Routes.FriendsScreen.route) {
            FriendsScreen(navController)
        }
        composable(Routes.HistorialScreen.route) {
            HistorialScreen(navController)
        }
        composable(Routes.ProfileScreen.route) {
            ProfileScreen(navController)
        }

        //HomeScreen
        composable(Routes.CreateGroupScreen.route) {
            CreateGroupScreen(navController)
        }
        composable(Routes.AddMemberScreen.route){
            AddMemberScreen(navController)
        }
        composable(Routes.PendingGroupsScreen.route){
            PendingGroupScreen(navController)
        }
        composable(Routes.PaymentMethodScreen.route){
            PaymentMethodScreen(navController)
        }

        //PerfilScreen
        composable(Routes.EditProfileScreen.route) {
            EditProfileScreen(navController)
        }
        composable(Routes.AccountScreen.route) {
            AccountScreen(navController)
        }
        composable(Routes.TermsScreen.route) {
            TermsScreen(navController)
        }
//        composable(Routes.SettingsScreen.route) {
//            SettingsScreen(navController)
//        }


        //LoginScreen
        composable(Routes.LoginScreen.route) {
            LoginScreen(navController)
        }
        composable(Routes.RegisterScreen.route) {
            RegisterScreen(navController)
        }


//        composable("recipe_screen/{categoryName}") { backStackEntry ->
//            val categoryName = backStackEntry.arguments?.getString("categoryName")
//            RecipeScreen(navController, categoryName)
//        }
//        composable("recipe_detail_screen/{recipeId}") { backStackEntry ->
//            val recipeId = backStackEntry.arguments?.getString("recipeId")
//            RecipeDetailScreen(navController, recipeId)
//        }
    }
}

