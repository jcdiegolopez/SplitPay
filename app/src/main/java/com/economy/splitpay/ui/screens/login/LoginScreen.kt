package com.economy.splitpay.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.economy.splitpay.R
import com.economy.splitpay.navigation.Routes
import com.economy.splitpay.ui.theme.primary
import com.economy.splitpay.ui.theme.secondaryLight
import com.economy.splitpay.viewmodel.login.LoginState
import com.economy.splitpay.viewmodel.login.LoginViewModel
import com.economy.splitpay.viewmodel.login.RegisterState
import com.economy.splitpay.viewmodel.login.RegisterViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, viewModel: LoginViewModel) {
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }

    val loginState by viewModel.loginState.collectAsState()

    LaunchedEffect(loginState) {
        when (loginState) {
            is LoginState.Success -> {
                navController.navigate(Routes.HomeScreen.route)
            }
            is LoginState.Error -> {
                val errorMessage = (loginState as LoginState.Error).error
                // Implementar un sistema para mostrar mensajes de error (snackbar, diálogo, etc.)
            }
            else -> {}

        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(primary)
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Imagen para el logo
        Image(
            painter = painterResource(id = R.drawable.logo2), // Reemplazar con el logo
            contentDescription = "Logo",
            modifier = Modifier.size(200.dp),
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Título de Iniciar Sesión
        Text(
            text = "Iniciar Sesión",
            color = Color.White,
            fontSize = 24.sp,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Campo de Correo
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo", color = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedIndicatorColor = secondaryLight,
                unfocusedIndicatorColor = Color.Gray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.LightGray
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de contraseña
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña", color = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedIndicatorColor = secondaryLight,
                unfocusedIndicatorColor = Color.Gray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.LightGray
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Botón de Ingresar
        Button(
            onClick = {
                viewModel.loginUser(email.text, password.text)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            colors = ButtonDefaults.buttonColors(containerColor = secondaryLight ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Ingresar", color = primary, style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Texto para registrarse
        TextButton(
            onClick = { navController.navigate(Routes.RegisterScreen.route) }
        ) {
            Text("¿No tienes cuenta? Regístrate", color = Color.White)
        }
    }

    if (loginState is LoginState.Loading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = secondaryLight)
        }
    }
}
