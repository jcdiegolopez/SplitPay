package com.economy.splitpay.repository

import com.economy.splitpay.model.User
import com.economy.splitpay.model.FriendRequest
import com.economy.splitpay.networking.firebase.AuthService
import com.economy.splitpay.networking.firebase.FirestoreService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepository(
    private val authService: AuthService = AuthService(),
    private val firestoreService: FirestoreService = FirestoreService()
) {

    // Registrar usuario con correo y contraseña, y guardar en Firestore
    suspend fun registerUser(email: String, password: String, firstname: String, lastname: String, tel: String, username: String) {
        try {
            val userId = authService.registerUser(email, password)
            val user = User(userId = userId, firstname = firstname, username = username, email = email, lastname = lastname, tel = tel)
            firestoreService.saveUser(user)
        } catch (e: Exception) {
            throw Exception("Error al registrar el usuario: ${e.message}")
        }
    }

    // Iniciar sesión con correo y contraseña
    suspend fun loginUser(email: String, password: String) {
        try {
            authService.loginUser(email, password)
        } catch (e: Exception) {
            throw Exception("Error al iniciar sesión: ${e.message}")
        }
    }

    // Inicio de sesión con Google
    suspend fun loginWithGoogle(idToken: String) {
        try {
            val userId = authService.loginWithGoogle(idToken)
            val user = firestoreService.getUserById(userId)
            if (user == null) {
                // Si el usuario no existe en Firestore, crearlo
                val newUser = User(userId = userId, email = authService.auth.currentUser?.email ?: "")
                firestoreService.saveUser(newUser)
            }
        } catch (e: Exception) {
            throw Exception("Error al iniciar sesión con Google: ${e.message}")
        }
    }

    // Verificar si el usuario está autenticado actualmente
    fun isUserLoggedIn(): Boolean {
        return authService.isUserLoggedIn()
    }

    // Cerrar sesión
    fun logout() {
        authService.logout()
    }

    // Enviar solicitud de amistad
    suspend fun sendFriendRequest(friendRequest: FriendRequest): String {
        return firestoreService.sendFriendRequest(friendRequest)
    }

    // Actualizar estado de solicitud de amistad
    suspend fun updateFriendRequestStatus(requestId: String, status: String) {
        firestoreService.updateFriendRequestStatus(requestId, status)
    }

    // Obtener todas las solicitudes de amistad de un usuario
    suspend fun getFriendRequests(userId: String): List<FriendRequest> {
        return try {
            firestoreService.getFriendRequests(userId)
        } catch (e: Exception) {
            throw Exception("Error al obtener solicitudes de amistad: ${e.message}")
        }
    }


    // Obtener el ID del usuario actualmente autenticado
    fun getCurrentUserId(): String {
        val user = authService.auth.currentUser
        if (user != null) {
            return user.uid
        } else {
            throw IllegalStateException("El usuario no está autenticado")
        }
    }

    // Buscar usuarios en Firestore por nombre de usuario
    suspend fun searchUsers(query: String): List<User> {
        return try {
            firestoreService.searchUsers(query)
        } catch (e: Exception) {
            throw Exception("Error al buscar usuarios: ${e.message}")
        }
    }

    // UserRepository.kt
    suspend fun getCurrentUser(): User {
        val userId = getCurrentUserId()
        return firestoreService.getUserById(userId)
            ?: throw Exception("No se encontró información del usuario actual")
    }


    fun observeFriendRequests(userId: String): Flow<List<FriendRequest>> {
        return firestoreService.observeFriendRequests(userId)
    }



}
