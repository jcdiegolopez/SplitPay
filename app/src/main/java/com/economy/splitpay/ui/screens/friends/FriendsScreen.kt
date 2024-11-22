package com.economy.splitpay.ui.screens.friends

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.economy.splitpay.model.User
import com.economy.splitpay.viewmodel.friends.FriendViewModel

@Composable
fun FriendsScreen(navController: NavController, viewModel: FriendViewModel) {
    // Observar solicitudes en tiempo real
    LaunchedEffect(viewModel) {
        viewModel.observeFriendRequests(viewModel.getCurrentUserId())
    }

    val friendRequests by viewModel.friendRequests.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val currentUserName by viewModel.currentUserName.collectAsState("")
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Campo de búsqueda
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { query ->
                searchQuery = query
                viewModel.searchUsers(query)
            },
            placeholder = { Text(text = "Encontrar amigos") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            shape = MaterialTheme.shapes.medium
        )

        // Resultados de búsqueda
        if (searchQuery.isNotEmpty()) {
            Text(
                text = "Resultados de búsqueda",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(searchResults) { user ->
                    SearchResultItem(
                        user = user,
                        onAddClick = { viewModel.sendFriendRequest(user.userId, currentUserName) }
                    )
                }
            }
        }

        // Título de la sección de solicitudes
        Text(
            text = "Solicitudes de amistad",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Lista de solicitudes de amistad
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(friendRequests) { friendRequest ->
                FriendRequestItem(
                    name = friendRequest.fromUserName,
                    onAcceptClick = { viewModel.acceptFriendRequest(friendRequest.requestId) },
                    onDeclineClick = { viewModel.declineFriendRequest(friendRequest.requestId) }
                )
            }
        }
    }
}


@Composable
fun FriendRequestItem(name: String, onAcceptClick: () -> Unit, onDeclineClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge
        )
        Row {
            Button(
                onClick = onAcceptClick,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Aceptar")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = onDeclineClick,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Declinar")
            }
        }
    }
}

@Composable
fun SearchResultItem(user: User, onAddClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = user.username,
            style = MaterialTheme.typography.bodyLarge
        )
        Button(
            onClick = onAddClick,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Text("Agregar")
        }
    }
}
