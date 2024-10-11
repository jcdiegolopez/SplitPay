package com.economy.splitpay.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.navigation.NavController

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.economy.splitpay.R
import com.economy.splitpay.navigation.Routes
import com.economy.splitpay.ui.theme.secondaryContainerLight
import com.economy.splitpay.ui.theme.secondaryLight

@Composable
fun HomeScreen(navController: NavController) {
    var showJoinGroupDialog by remember { mutableStateOf(false) } // Estado para mostrar el diálogo
    val textColor = MaterialTheme.colorScheme.onSurface

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Botones "Crear un grupo" y "Unirse a un grupo"
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { navController.navigate(Routes.CreateGroupScreen.route) },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = secondaryLight),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Crear un grupo", color = Color.Black, style = MaterialTheme.typography.bodyLarge)
            }

            Button(
                onClick = { showJoinGroupDialog = true }, // Mostrar diálogo
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF333947)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Unirse a un grupo",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sección de "Tus Grupos"
        Text(
            text = "Tus Grupos",
            style = MaterialTheme.typography.headlineMedium.copy(
                color = textColor
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // LazyRow para mostrar los grupos
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            items(listOf("Fiesta Genser", "Cena mamá", "Family", "Amigos")) { group ->
                GroupCard(title = group, backgroundColor = MaterialTheme.colorScheme.outline)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Sección de notificaciones con espaciado inferior
        Text(
            text = "Notificaciones",
            style = MaterialTheme.typography.headlineMedium.copy(
                color = textColor
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        NotificationItem(message = "Tienes una invitación para unirte")
        NotificationItem(message = "Un nuevo amigo te espera")
    }

    // Mostrar el popup si showJoinGroupDialog es true
    if (showJoinGroupDialog) {
        JoinGroupDialog(
            onDismiss = { showJoinGroupDialog = false },
            onJoinGroup = { code ->
                println("Código de grupo ingresado: $code")
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinGroupDialog(onDismiss: () -> Unit, onJoinGroup: (String) -> Unit) {
    var groupCode by remember { mutableStateOf(TextFieldValue("")) } // Estado para el código del grupo

    // Usando el BasicAlertDialog de la librería ExperimentalMaterial3Api
    BasicAlertDialog(
        modifier = Modifier.background(Color.White, shape = RoundedCornerShape(8.dp)),
        onDismissRequest = onDismiss,
        properties = DialogProperties(), // Puedes agregar otras propiedades aquí si es necesario
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Título y botón de cerrar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Unirse a un grupo",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.Black
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cerrar",
                            tint = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Campo de texto para el código del grupo
                TextField(
                    value = groupCode,
                    onValueChange = { groupCode = it },
                    label = { Text("Ingresa el código del grupo") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botón "Unirse"
                Button(
                    onClick = {
                        onJoinGroup(groupCode.text) // Acción de unirse al grupo
                        onDismiss() // Cerrar el diálogo
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = secondaryLight),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Unirse", color = Color.Black, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    )
}

@Composable
fun NotificationItem(message: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Acción al hacer clic en la notificación */ }
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = message, style = MaterialTheme.typography.bodyLarge)
        Text(text = "→", fontSize = 32.sp)
    }
}

@Composable
fun GroupCard(title: String, backgroundColor: Color) {
    val initial = title.first().toString() // Obtener la primera letra del título

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(120.dp) // Ancho de cada tarjeta
            .padding(vertical = 8.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(100.dp) // Tamaño del recuadro
                .background(color = backgroundColor, shape = RoundedCornerShape(8.dp))
        ) {
            Text(
                text = initial,
                style = MaterialTheme.typography.headlineMedium.copy( // Cambié a un estilo de título
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge.copy( // Usando `bodyLarge` para el título del grupo
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
            ),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}