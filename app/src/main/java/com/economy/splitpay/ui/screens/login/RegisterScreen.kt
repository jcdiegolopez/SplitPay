package com.economy.splitpay.ui.screens.register

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
import com.economy.splitpay.viewmodel.login.RegisterState
import com.economy.splitpay.viewmodel.login.RegisterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController, viewModel: RegisterViewModel) {
    var firstName by remember { mutableStateOf(TextFieldValue("")) }
    var lastName by remember { mutableStateOf(TextFieldValue("")) }
    var phoneNumber by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var username by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }

    val registerState by viewModel.registerState.collectAsState()

    // Mostrar feedback basado en el estado
    LaunchedEffect(registerState) {
        when (registerState) {
            is RegisterState.Success -> {
                // Mostrar un mensaje de éxito y navegar
                navController.navigate(Routes.LoginScreen.route)
            }
            is RegisterState.Error -> {
                // Mostrar un error con un mensaje de error
                val errorMessage = (registerState as RegisterState.Error).error
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
            modifier = Modifier.size(150.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Título de Registro
        Text(
            text = "Registro",
            color = Color.White,
            fontSize = 24.sp,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de Nombre
        TextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("Nombre", color = Color.Gray) },
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

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de Apellido
        TextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Apellido", color = Color.Gray) },
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

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de Número de Teléfono
        TextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Número de Teléfono", color = Color.Gray) },
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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de Correo Electrónico
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo Electrónico (Gmail)", color = Color.Gray) },
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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de Nombre de Usuario
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Nombre de Usuario", color = Color.Gray) },
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

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de Contraseña
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

        Spacer(modifier = Modifier.height(24.dp))

        // Botón de Registro
        Button(
            onClick = {
                viewModel.registerUser(
                    email = email.text,
                    password = password.text,
                    firstname = firstName.text,
                    lastname = lastName.text,
                    tel = phoneNumber.text,
                    username = username.text
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            colors = ButtonDefaults.buttonColors(containerColor = secondaryLight),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Registrarse", color = primary, style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = { navController.navigate(Routes.LoginScreen.route) }
        ) {
            Text("¿Ya tienes cuenta? Inicia Sesión", color = Color.White)
        }
    }

    // Mostrar indicador de carga si el estado es Loading
    if (registerState is RegisterState.Loading) {
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
