package com.economy.splitpay.viewmodel.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.economy.splitpay.model.FriendRequest
import com.economy.splitpay.model.User
import com.economy.splitpay.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FriendViewModel(
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    private val _friendRequests = MutableStateFlow<List<FriendRequest>>(emptyList())
    val friendRequests: StateFlow<List<FriendRequest>> get() = _friendRequests

    private val _searchResults = MutableStateFlow<List<User>>(emptyList())
    val searchResults: StateFlow<List<User>> get() = _searchResults

    private val _currentUserName = MutableStateFlow("")
    val currentUserName: StateFlow<String> get() = _currentUserName

    init {
        loadCurrentUserName()
    }

    // Cargar solicitudes de amistad para el usuario actual
    fun loadFriendRequests(userId: String) {
        viewModelScope.launch {
            try {
                val requests = userRepository.getFriendRequests(userId)
                println("Amistades obtenidas del repositorio: $requests")
                _friendRequests.value = requests // Correcto
            } catch (e: Exception) {
                println("Error al cargar solicitudes de amistad: ${e.message}")
                _friendRequests.value = emptyList() // Opcional para manejar errores
            }
        }
    }



    // Aceptar una solicitud de amistad
    fun acceptFriendRequest(requestId: String) {
        viewModelScope.launch {
            try {
                userRepository.updateFriendRequestStatus(requestId, "accepted")
                // Recargar solicitudes
                loadFriendRequests(userRepository.getCurrentUserId())
            } catch (e: Exception) {
                println("Error al aceptar solicitud de amistad: ${e.message}")
            }
        }
    }

    // Declinar una solicitud de amistad
    fun declineFriendRequest(requestId: String) {
        viewModelScope.launch {
            try {
                userRepository.updateFriendRequestStatus(requestId, "declined")
                // Recargar solicitudes
                loadFriendRequests(userRepository.getCurrentUserId())
            } catch (e: Exception) {
                // Manejar errores
            }
        }
    }

    // Buscar usuarios por nombre
    fun searchUsers(query: String) {
        viewModelScope.launch {
            try {
                val results = userRepository.searchUsers(query)
                _searchResults.value = results
            } catch (e: Exception) {
                // Manejar errores
            }
        }
    }

    // Enviar solicitud de amistad
    fun sendFriendRequest(toUserId: String, fromUserName: String) {
        viewModelScope.launch {
            try {
                val currentUserId = userRepository.getCurrentUserId()
                val friendRequest = FriendRequest(
                    fromUserId = currentUserId,
                    toUserId = toUserId,
                    fromUserName = fromUserName,
                    dateSent = System.currentTimeMillis().toString()
                )
                userRepository.sendFriendRequest(friendRequest)
                println(userRepository.sendFriendRequest(friendRequest))
                println("Solicitud de amistad enviada: $friendRequest")
            } catch (e: Exception) {
                // Manejar errores
            }
        }
    }

    private fun loadCurrentUserName() {
        viewModelScope.launch {
            try {
                val currentUser = userRepository.getCurrentUser()
                _currentUserName.value = currentUser.username
            } catch (e: Exception) {
                // Manejar errores
            }
        }
    }

    fun getCurrentUserId(): String {
        return userRepository.getCurrentUserId()
    }

    fun observeFriendRequests(userId: String) {
        viewModelScope.launch {
            userRepository.observeFriendRequests(userId).collect { requests ->
                // Filtrar solo las solicitudes con estado "pending"
                val pendingRequests = requests.filter { it.status == "pending" }
                println("Solicitudes pendientes en ViewModel: $pendingRequests")
                _friendRequests.value = pendingRequests
            }
        }
    }


}
