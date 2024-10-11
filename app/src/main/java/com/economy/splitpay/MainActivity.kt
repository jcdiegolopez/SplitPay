package com.economy.splitpay

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.economy.splitpay.navigation.AppNavigation
import com.economy.splitpay.navigation.BottomBar
import com.economy.splitpay.navigation.Routes
import com.economy.splitpay.navigation.TopBar
import com.economy.splitpay.ui.theme.SplitpayTheme
import com.economy.splitpay.ui.theme.backgroundLight

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SplitpayTheme {
                val navController = rememberNavController()
                MainApp(navController)
            }

        }
    }
}

@Composable
fun MainApp(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val title = when (currentRoute) {
        Routes.HomeScreen.route -> "Inicio"
        Routes.ProfileScreen.route -> "Perfil"
        Routes.FriendsScreen.route -> "Amigos"
        Routes.HistorialScreen.route -> "Historial"
        Routes.CreateGroupScreen.route -> "Crear Grupo"

        else -> "SplitPay"
    }

    Scaffold(
        topBar = {
            val showBackButton = currentRoute != Routes.HomeScreen.route && currentRoute != Routes.ProfileScreen.route
                    && currentRoute != Routes.FriendsScreen.route && currentRoute != Routes.HistorialScreen.route
            TopBar(navController = navController, title = title, showBackButton = showBackButton)
        },
        bottomBar = {
            BottomBar(navController = navController, currentRoute = currentRoute)
        }
    ) { innerPadding ->
        AppNavigation(navController = navController, innerPadding = innerPadding )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun GreetingPreview() {
    SplitpayTheme {
        MainApp(navController = rememberNavController())
    }}
