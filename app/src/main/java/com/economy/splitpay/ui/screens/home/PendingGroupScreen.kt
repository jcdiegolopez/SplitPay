package com.economy.splitpay.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.economy.splitpay.navigation.Routes

@Composable
fun PendingGroupScreen(navController: NavController) {
    var showModifyDialog by remember { mutableStateOf(false) } // Estado para mostrar el diálogo

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
                onClick = { showModifyDialog = true }, // Mostrar el diálogo al presionar "Modificar"
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
                onClick = { navController.navigate(Routes.PaymentMethodScreen.route) },
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

    if (showModifyDialog) {
        ModifyAmountDialog(
            onDismiss = { showModifyDialog = false },
            onConfirm = { selectedPerson, amount ->
                println("Persona seleccionada: $selectedPerson, Nuevo monto: $amount")
            }
        )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyAmountDialog(onDismiss: () -> Unit, onConfirm: (String, String) -> Unit) {
    var selectedPerson by remember { mutableStateOf("") } // Persona seleccionada
    var expanded by remember { mutableStateOf(false) } // Control del DropdownMenu
    var newAmount by remember { mutableStateOf(TextFieldValue("")) } // Monto nuevo

    // Lista de personas (puedes adaptarla según tu lógica)
    val participants = listOf("Jenny B", "Jimmy C", "Kristen D", "Lucy E")

    BasicAlertDialog(
        modifier = Modifier.background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp)),
        onDismissRequest = onDismiss,
        content = {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                // Título del diálogo
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Modificar Monto",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = MaterialTheme.colorScheme.onSurface)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // DropdownMenu para seleccionar a una persona
                Text("Selecciona a una persona", style = MaterialTheme.typography.bodyLarge)

                // Usar ExposedDropdownMenuBox para gestionar el Dropdown de manera más precisa
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    TextField(
                        value = selectedPerson,
                        onValueChange = { selectedPerson = it },
                        label = { Text("Selecciona un participante") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor() // Hace que el menú aparezca debajo del TextField
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        participants.forEach { person ->
                            DropdownMenuItem(
                                text = { Text(text = person) },
                                onClick = {
                                    selectedPerson = person
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Input para ingresar el nuevo monto
                TextField(
                    value = newAmount,
                    onValueChange = { newAmount = it },
                    label = { Text("Ingresa el nuevo monto") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp))
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botón para confirmar el cambio
                Button(
                    onClick = {
                        if (selectedPerson.isNotEmpty() && newAmount.text.isNotEmpty()) {
                            onConfirm(selectedPerson, newAmount.text)
                            onDismiss() // Cerrar el diálogo
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF127DED))
                ) {
                    Text(text = "Aceptar", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    )
}
