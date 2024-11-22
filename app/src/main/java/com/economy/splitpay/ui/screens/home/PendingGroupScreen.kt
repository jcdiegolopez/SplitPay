package com.economy.splitpay.ui.screens.home

import android.util.Log
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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.economy.splitpay.model.Member
import com.economy.splitpay.navigation.Routes
import com.economy.splitpay.viewmodel.group.GroupState
import com.economy.splitpay.viewmodel.group.GroupViewModel
import com.economy.splitpay.viewmodel.group.PendingGroupState
import com.economy.splitpay.viewmodel.group.PendingGroupViewModel

fun calculatePendingAmount(totalAmount: Double, members: List<Member>): Double {
    var pendingAmount = totalAmount
    members.forEach { member ->
        pendingAmount -= member.assignedAmount
    }
    return pendingAmount
}

@Composable
fun PendingGroupScreen(navController: NavController, viewModel: PendingGroupViewModel, groupId: String) {
    // Estado para observar los datos del grupo
    val groupState by viewModel.groupState.collectAsState()

    // Cargar el grupo al iniciar la pantalla
    LaunchedEffect(key1 = groupId) {
        viewModel.loadGroupWithLeaderStatus(groupId)
    }

    var showModifyDialog by remember { mutableStateOf(false) } // Estado para mostrar el diálogo

    when (groupState) {
        is PendingGroupState.Loading -> {
            Log.d("PendingGroupScreen", "Loading desde screen: $groupId")
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is PendingGroupState.GroupLoaded -> {
            Log.d("PendingGroupScreen", "Grupo cargado desde screen: ${(groupState as PendingGroupState.GroupLoaded).group}")
            val group = (groupState as PendingGroupState.GroupLoaded).group

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Token -${group.token}",
                    color = Color(0xFF127DED),
                    style = MaterialTheme.typography.headlineMedium.copy(fontSize = 24.sp),
                    modifier = Modifier.padding(bottom = 4.dp).align(alignment = Alignment.CenterHorizontally)
                )
                Text(
                    text = "Pendiente Q${calculatePendingAmount(group.totalAmount, group.members)}",
                    style = MaterialTheme.typography.headlineMedium.copy(fontSize = 24.sp),
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Text(
                    text = "Monto total: Q${group.totalAmount}",
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "Participantes",
                    style = MaterialTheme.typography.titleSmall.copy(fontSize = 20.sp),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                group.members.forEach { member ->
                    ParticipantItem(
                        name = member.name,
                        amount = "Q${member.assignedAmount}",
                        status = if (member.accepted ) "Aceptado"  else "Pendiente"
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                if ((groupState as PendingGroupState.GroupLoaded).isLeader) {
                    // Vista para el líder
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = { showModifyDialog = true },
                            colors = ButtonDefaults.buttonColors(Color(0xFF127DED)),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(text = "Modificar", style = MaterialTheme.typography.titleSmall.copy(fontSize = 16.sp))
                        }
                        Button(
                            onClick = { navController.navigate(Routes.PaymentMethodScreen.route) },
                            colors = ButtonDefaults.buttonColors(Color(0xFFF0F2F5)),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(text = "Confirmar", color = Color.Black, style = MaterialTheme.typography.titleSmall.copy(fontSize = 16.sp))
                        }
                    }
                } else {
                    // Vista para miembros
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = { viewModel.updateMemberAcceptance(groupId,  true) },
                            colors = ButtonDefaults.buttonColors(Color(0xFF127DED)),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(text = "Aceptar", style = MaterialTheme.typography.titleSmall.copy(fontSize = 16.sp))
                        }
                        Button(
                            onClick = { viewModel.updateMemberAcceptance(groupId, false) },
                            colors = ButtonDefaults.buttonColors(Color(0xFFFF4444)),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(text = "Rechazar", color = Color.White, style = MaterialTheme.typography.titleSmall.copy(fontSize = 16.sp))
                        }
                    }
                }
            }

            if (showModifyDialog) {
                ModifyAmountDialog(
                    onDismiss = { showModifyDialog = false },
                    onConfirm = { selectedPerson, amount ->
                        viewModel.updateMemberAmount(groupId, selectedPerson, amount.toDouble())
                    }
                )
            }
        }

        is PendingGroupState.Error -> {
            Log.e("PendingGroupScreen", (groupState as PendingGroupState.Error).message)
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = (groupState as PendingGroupState.Error).message,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun ParticipantItem(name: String, amount: String, status: String) {
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
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
            )
        }
        Text(text = "$amount · $status", style = MaterialTheme.typography.bodyLarge)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyAmountDialog(onDismiss: () -> Unit, onConfirm: (String, String) -> Unit) {
    var selectedPerson by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var newAmount by remember { mutableStateOf(TextFieldValue("")) }

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
                Row(
                    modifier = Modifier.fillMaxWidth(),
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

                Text("Selecciona a una persona", style = MaterialTheme.typography.bodyLarge)

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
                            .menuAnchor()
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

                TextField(
                    value = newAmount,
                    onValueChange = { newAmount = it },
                    label = { Text("Ingresa el nuevo monto") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (selectedPerson.isNotEmpty() && newAmount.text.isNotEmpty()) {
                            onConfirm(selectedPerson, newAmount.text)
                            onDismiss()
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
