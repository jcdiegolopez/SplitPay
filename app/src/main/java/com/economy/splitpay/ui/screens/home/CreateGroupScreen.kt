package com.economy.splitpay.ui.screens.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.economy.splitpay.model.Member
import com.economy.splitpay.model.User
import com.economy.splitpay.viewmodel.group.GroupState
import com.economy.splitpay.viewmodel.group.GroupViewModel

@Composable
fun CreateGroupScreen(
    navController: NavController,
    viewModel: GroupViewModel
) {
    var groupName by remember { mutableStateOf("") }
    var totalAmount by remember { mutableStateOf("") }
    val members = remember { mutableStateListOf<Member>() }
    var showPopup by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val groupState by viewModel.groupState.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Título
        Text(
            text = "Crear Grupo",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        // Nombre del grupo
        OutlinedTextField(
            value = groupName,
            onValueChange = { groupName = it },
            label = { Text("Nombre del Grupo") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Monto total
        OutlinedTextField(
            value = totalAmount,
            onValueChange = { totalAmount = it },
            label = { Text("Monto Total (Q)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de participantes
        Text(
            text = "Participantes:",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(members) { member ->
                ParticipantItem(
                    member = member,
                    onAmountChange = { newAmount ->
                        member.assignedAmount = newAmount
                    }
                )
            }

            // Botón para añadir participante
            item {
                AddParticipantItem {
                    showPopup = true
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón de crear grupo
        Button(
            onClick = {
                val amount = totalAmount.toDoubleOrNull()
                if (groupName.isBlank() || amount == null) {
                    Toast.makeText(context, "Ingrese todos los datos correctamente", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.createGroup(
                        groupName = groupName,
                        totalAmount = amount,
                        members = members.toList(),
                        navController = navController
                    )

                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF5C045)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Realizar", color = Color.Black, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
        }
    }
    if (showPopup) {
        AddFriendsPopup(
            viewModel = viewModel,
            onDismiss = { showPopup = false },
            onAddMembers = { newMembers ->
                members.addAll(newMembers)
            }
        )
    }
}

@Composable
fun ParticipantItem(
    member: Member,
    onAmountChange: (Double) -> Unit
) {
    var localAmount by remember { mutableStateOf(member.assignedAmount.toString()) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = member.name.ifBlank { "Sin nombre" },
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            OutlinedTextField(
                value = localAmount,
                onValueChange = {
                    localAmount = it
                    onAmountChange(it.toDoubleOrNull() ?: 0.0)
                },
                label = { Text("Monto Asignado") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.width(120.dp)
            )
        }
        Text(
            text = "Estado: ${member.paymentStatus}",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun AddParticipantItem(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Añadir participante",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E3EB))
        ) {
            Text(text = "Invitar", color = Color.Black, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun AddFriendsPopup(
    viewModel: GroupViewModel,
    onDismiss: () -> Unit,
    onAddMembers: (List<Member>) -> Unit
) {
    val state by viewModel.groupState.collectAsState()
    val selectedFriends = remember { mutableStateListOf<User>() }// Lista de amigos seleccionados

    LaunchedEffect(Unit) {
        viewModel.loadFriends()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable(onClick = onDismiss, indication = null, interactionSource = remember { MutableInteractionSource() })
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.Center)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Seleccionar Amigos",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                when (state) {
                    is GroupState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally), color =Color(0xFFF5C045))
                    }
                    is GroupState.UserFriendsLoaded -> {
                        val friends = (state as GroupState.UserFriendsLoaded).userFriends
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(friends) { friend ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = friend.firstname + " " + friend.lastname,
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Checkbox(
                                        checked = selectedFriends.contains(friend),
                                        onCheckedChange = { isChecked ->
                                            if (isChecked) {
                                                selectedFriends.add(friend)
                                            } else {
                                                selectedFriends.remove(friend)
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                    is GroupState.Error -> {
                        Text(
                            text = (state as GroupState.Error).error,
                            color = Color.Red,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    else -> Unit
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF5C045)),
                        shape = RoundedCornerShape(8.dp),
                        onClick = {
                        val members = selectedFriends.map { friend ->
                            Member(
                                userId = friend.userId,
                                name = friend.firstname + " " + friend.lastname,
                                assignedAmount = 0.0,
                                paymentStatus = "Pendiente"
                            )
                        }
                        onAddMembers(members)
                        onDismiss()
                    }) {
                        Text("Finalizar", color = Color.Black, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

