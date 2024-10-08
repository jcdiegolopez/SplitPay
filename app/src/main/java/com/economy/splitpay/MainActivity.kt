package com.economy.splitpay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
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
import com.economy.splitpay.navigation.TopBar
import com.economy.splitpay.ui.theme.SplitpayTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            MainApp(navController)
        }
    }
}

@Composable
fun MainApp(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Scaffold(
        topBar = {
            val showBackButton = currentRoute != "home_screen"
            TopBar(navController = navController, title = "SplitPay", showBackButton = showBackButton)
        },
        bottomBar = {
            BottomBar(navController)
        }
    ) { innerPadding ->
        AppNavigation(navController = navController, innerPadding = innerPadding )
    }
}
