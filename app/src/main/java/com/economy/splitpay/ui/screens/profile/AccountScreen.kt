package com.economy.splitpay.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.economy.splitpay.ui.theme.secondaryLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(navController: NavController) {
    var email by remember { mutableStateOf("usuario@example.com") }
    var phoneNumber by remember { mutableStateOf("123-456-7890") }
    var password by remember { mutableStateOf("********") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        // Fields for Account Information
        AccountField(label = "Correo electrónico", value = email, keyboardType = KeyboardType.Email)
        AccountField(label = "Número de teléfono", value = phoneNumber, keyboardType = KeyboardType.Phone)
        AccountField(label = "Contraseña", value = password, keyboardType = KeyboardType.Password, isPassword = true)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountField(label: String, value: String, keyboardType: KeyboardType, isPassword: Boolean = false) {
    var fieldValue by remember { mutableStateOf(value) }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)) {
        Text(
            text = label,
            fontWeight = FontWeight.SemiBold,
            color = Color.Gray,
            fontSize = 14.sp
        )
        TextField(
            value = fieldValue,
            onValueChange = { fieldValue = it },
            placeholder = { Text(text = "Editar $label", color = Color.LightGray) },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedIndicatorColor = secondaryLight,
                unfocusedIndicatorColor = Color.LightGray,
            ),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )
    }
}
