package com.economy.splitpay.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.economy.splitpay.ui.theme.navBarColor
import com.economy.splitpay.ui.theme.onPrimaryLight
import com.economy.splitpay.ui.theme.primaryLight
import com.economy.splitpay.ui.theme.secondaryLight


@Composable
fun BottomBar(navController: NavController, currentRoute: String?) {
    val items = listOf(
        BottomNavItem("Inicio", Icons.Filled.Home, Routes.HomeScreen.route),
        BottomNavItem("Amigos", Icons.Filled.Add, Routes.FriendsScreen.route),
        BottomNavItem("Historial", Icons.Filled.List, Routes.HistorialScreen.route),
        BottomNavItem("Perfil", Icons.Filled.Person, Routes.ProfileScreen.route)
    )

    BottomAppBar(
        modifier = Modifier.fillMaxWidth()
        , containerColor = navBarColor
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            items.forEach { item ->
                IconButton(
                    onClick = {
                    navController.navigate(item.route)
                }) {
                    Icon(imageVector = item.icon,
                        modifier = Modifier.size(32.dp),
                        contentDescription = item.label,
                        tint = if (currentRoute == item.route) secondaryLight else onPrimaryLight
                    )
                }
            }
        }
    }
}

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val route: String
)