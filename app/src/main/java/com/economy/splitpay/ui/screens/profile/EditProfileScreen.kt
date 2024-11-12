package com.economy.splitpay.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.economy.splitpay.R
import com.economy.splitpay.ui.theme.secondaryLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(navController: NavController) {
    var firstName by remember { mutableStateOf("Diego") }
    var lastName by remember { mutableStateOf("Rosales") }
    var username by remember { mutableStateOf("diegorosales48") }
    var shortDescription by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        // Profile Picture with Edit Icon
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = R.drawable.dog), // Reemplazar con la imagen de perfil
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = "Cambiar foto",
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(32.dp)
                    .background(Color(0x55000000), shape = CircleShape)
                    .padding(4.dp),
                tint = Color.White
            )
        }

        Text(
            text = "Cambiar foto",
            color = Color.Gray,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(top = 8.dp)
                .align(Alignment.CenterHorizontally)
                .clickable { /* Acción para cambiar foto */ }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Editable Fields
        ProfileField(
            label = "Nombre",
            value = "$firstName $lastName",
            onValueChange = { newValue ->
                // Actualiza el nombre y apellido a partir del valor completo
                val parts = newValue.split(" ")
                firstName = parts.firstOrNull() ?: ""
                lastName = parts.getOrNull(1) ?: ""
            }
        )

        ProfileField(
            label = "Nombre de usuario",
            value = username,
            onValueChange = { username = it }
        )

        ProfileField(
            label = "Descripción corta",
            value = shortDescription,
            placeholder = "Añadir una descripción corta",
            onValueChange = { shortDescription = it }
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileField(
    label: String,
    value: String,
    placeholder: String = "",
    onValueChange: (String) -> Unit
) {
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
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = Color.LightGray) },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedIndicatorColor = secondaryLight,
                unfocusedIndicatorColor = Color.LightGray,
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
