package com.economy.splitpay.repository

import com.economy.splitpay.model.User
import com.economy.splitpay.networking.firebase.AuthService
import com.economy.splitpay.networking.firebase.FirestoreService



class UserRepository(
    private val authService: AuthService = AuthService(),
    private val firestoreService: FirestoreService = FirestoreService()
) {

    // Registrar usuario con correo y contraseña, y guardar en Firestore
    suspend fun registerUser(email: String, password: String, firstname: String,lastname: String, tel: String,username: String) {
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
}