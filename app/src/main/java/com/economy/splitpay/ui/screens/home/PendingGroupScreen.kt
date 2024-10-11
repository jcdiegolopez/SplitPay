package com.economy.splitpay.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun PendingGroupScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Texto Pendiente
        Text(
            text = "Pendiente Q345.00",
            style = MaterialTheme.typography.headlineMedium.copy(fontSize = 24.sp), // Texto más pequeño
            modifier = Modifier
                .padding(bottom = 4.dp)
                .align(Alignment.Start) // Alineación izquierda
        )

        // Texto Monto total
        Text(
            text = "Monto total: Q1,234.00",
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp), // Un poco más pequeño
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(Alignment.Start) // Alineación izquierda
        )

        // Título Participantes
        Text(
            text = "Participantes",
            style = MaterialTheme.typography.titleSmall.copy(fontSize = 20.sp), // Un poco más pequeño
            modifier = Modifier
                .padding(bottom = 8.dp)
                .align(Alignment.Start) // Alineación izquierda
        )

        // Lista de participantes
        ParticipantItem(name = "Jenny B", amount = "Q123.00", status = "Aceptado", trust = 9)
        ParticipantItem(name = "Jimmy C", amount = "Q123.00", status = "Aceptado", trust = 8)
        ParticipantItem(name = "Kristen D", amount = "Q123.00", status = "Pendiente", trust = 7)
        ParticipantItem(name = "Lucy E", amount = "Q123.00", status = "Aceptado", trust = 8)

        Spacer(modifier = Modifier.weight(1f))

        // Botones Modificar y Confirmar
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp) // Espacio entre botones
        ) {
            Button(
                onClick = { /* Handle Modify action */ },
                colors = ButtonDefaults.buttonColors(Color(0xFF127DED)),
                modifier = Modifier
                    .weight(1f) // Ancho dinámico
                    .height(50.dp), // Altura ajustada
                shape = RoundedCornerShape(8.dp) // Bordes menos redondeados
            ) {
                Text(
                    text = "Modificar",
                    style = MaterialTheme.typography.titleSmall.copy(fontSize = 16.sp) // Texto más grande
                )
            }
            Button(
                onClick = { navController.navigate("payment_method_screen") },
                colors = ButtonDefaults.buttonColors(Color(0xFFF0F2F5)),
                modifier = Modifier
                    .weight(1f) // Ancho dinámico
                    .height(50.dp), // Altura ajustada
                shape = RoundedCornerShape(8.dp) // Bordes menos redondeados
            ) {
                Text(
                    text = "Confirmar",
                    color = Color.Black,
                    style = MaterialTheme.typography.titleSmall.copy(fontSize = 16.sp) // Texto más grande
                )
            }
        }
    }
}

@Composable
fun ParticipantItem(name: String, amount: String, status: String, trust: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold) // Semibold para los nombres
            )
            Text(
                text = "Confianza: $trust",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
        }
        Text(text = "$amount · $status", style = MaterialTheme.typography.bodyLarge)
    }
}