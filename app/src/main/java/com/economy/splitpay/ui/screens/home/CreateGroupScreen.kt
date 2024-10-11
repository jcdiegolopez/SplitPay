package com.economy.splitpay.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.economy.splitpay.R
import com.economy.splitpay.navigation.Routes

@Composable
fun CreateGroupScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Título de la pantalla
        Text(
            text = "Cena mamá",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        // Campo de texto para mostrar el total pendiente
        OutlinedTextField(
            value = "Total pendiente: Q120.00",
            onValueChange = { /* Acción si es editable */ },
            enabled = false, // Para que sea solo de lectura
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de participantes
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(participants) { participant ->
                ParticipantItem(
                    name = participant.name,
                    amount = participant.amount
                )
            }

            // Opción para añadir participante
            item {
                AddParticipantItem(navController)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón de realizar
        Button(
            onClick = { navController.navigate(Routes.PendingGroupsScreen.route) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF5C045)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Realizar", color = Color.Black, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun ParticipantItem(name: String, amount: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSurface)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Q$amount",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Button(
            onClick = { /* Acción al cambiar el monto */ },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E3EB))
        ) {
            Text(text = "Change", color = Color.Black, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun AddParticipantItem(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Añadir participante",
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Añadir participante",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Button(
            onClick = { navController.navigate("add_member_screen") },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E3EB))
        ) {
            Text(text = "Invite", color = Color.Black, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

// Lista de participantes para el ejemplo
val participants = listOf(
    Participant("Tú", 150.0),
    Participant("Liam", 50.0),
    Participant("Noah", 50.0),
    Participant("Oliver", 120.0)
)

data class Participant(val name: String, val amount: Double)
