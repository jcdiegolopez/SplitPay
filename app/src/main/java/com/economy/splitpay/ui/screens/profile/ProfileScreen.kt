package com.economy.splitpay.ui.screens.profile

import android.media.Image
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import com.economy.splitpay.R
import com.economy.splitpay.navigation.Routes
import com.economy.splitpay.ui.theme.secondaryLight

@Composable
fun ProfileScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Profile Image
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Imagen de perfil",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // User Info
        Text(
            text = "Neto Bran",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "@ironbran",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Puntaje de confianza: 8/10",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { navController.navigate(Routes.EditProfileScreen.route) },
//                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = secondaryLight)
            ) {
                Text("Editar Perfil", color = Color.Black)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { /* Acción de invitar amigos */ },
//                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF333947))
            ) {
                Text("Invitar amigos", color = Color.White)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { navController.navigate(Routes.FriendsScreen.route) },
//                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF333947))
            ) {
                Icon(
                    imageVector = Icons.Default.PersonAddAlt,
                    contentDescription = "Icono de añadir",
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Configurations List
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ConfigOption("Cuenta", Icons.Default.AccountCircle, Icons.Default.KeyboardArrowRight) {
                navController.navigate(Routes.AccountScreen.route)
            }
            ConfigOption(
                "Métodos de pago",
                Icons.Default.CreditCard,
                Icons.Default.KeyboardArrowRight
            ) {
                navController.navigate(Routes.AccountScreen.route)
            }
            ConfigOption(
                "Notificaciones",
                Icons.Default.Notifications,
                Icons.Default.KeyboardArrowRight
            ) {
                navController.navigate(Routes.SettingsScreen.route)
            }
            ConfigOption(
                "Términos y condiciones",
                Icons.Default.Info,
                Icons.Default.KeyboardArrowRight
            ) {
                navController.navigate(Routes.TermsScreen.route)
            }
            ConfigOption(
                "Cerrar Sesión",
                Icons.Default.ExitToApp,
                Icons.Default.KeyboardArrowRight
            ) {
                // Acción de cerrar sesión
                navController.navigate(Routes.LoginScreen.route) {
                    popUpTo(Routes.HomeScreen.route) { inclusive = true }
                }
            }
        }
    }
}

@Composable
fun ConfigOption(title: String, leftIcon: ImageVector, rightIcon: ImageVector, onclick: () -> Unit) {
    Button(
        onClick = onclick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent), // Botón sin color de fondo
        contentPadding = PaddingValues(0.dp) // Sin padding para que se vea como una fila normal
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left icon
            Icon(
                imageVector = leftIcon,
                contentDescription = "Icono de configuración",
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Title
            Text(
                text = title,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Right icon
            Icon(
                imageVector = rightIcon,
                contentDescription = "Ir a configuración",
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}