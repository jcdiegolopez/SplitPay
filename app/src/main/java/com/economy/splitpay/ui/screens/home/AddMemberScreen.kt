package com.economy.splitpay.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun AddMemberScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Campo de búsqueda de contactos
        OutlinedTextField(
            value = "",
            onValueChange = { /* Acción de búsqueda */ },
            placeholder = { Text(text = "Nombre, número, correo.") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(8.dp)
        )

        // Título de la sección de contactos
        Text(
            text = "Contactos",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Lista de contactos
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(contacts) { contact ->
                ContactItem(
                    name = contact.name,
                    phone = contact.phone
                )
            }
        }
    }
}

@Composable
fun ContactItem(name: String, phone: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = phone,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Button(
            onClick = { /* Acción al agregar al grupo */ },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E3EB))
        ) {
            Text(text = "Add to group", color = Color.Black, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

// Lista de contactos para el ejemplo
val contacts = listOf(
    Contact("Abdul Lee", "(437) 592-4342"),
    Contact("Adrian Tang", "(740) 235-0475"),
    Contact("Sarah Peterman", "(846) 213-1485"),
    Contact("Aida He", "(849) 232-4235"),
    Contact("Alex Kuang", "(904) 123-2345")
)

data class Contact(val name: String, val phone: String)
