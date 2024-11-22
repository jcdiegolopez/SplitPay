package com.economy.splitpay.networking.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class AuthService {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Registrar usuario con correo y contraseña
    suspend fun registerUser(email: String, password: String): String {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            result.user?.uid ?: throw Exception("No se pudo obtener el UID del usuario registrado.")
        } catch (e: Exception) {
            throw Exception("Error al registrar el usuario: ${e.message}")
        }
    }

    // Iniciar sesión con correo y contraseña
    suspend fun loginUser(email: String, password: String): String {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            result.user?.uid ?: throw Exception("No se pudo obtener el UID del usuario autenticado.")
        } catch (e: Exception) {
            throw Exception("Error al iniciar sesión: ${e.message}")
        }
    }

    // Registro e inicio de sesión con Google
    suspend fun loginWithGoogle(idToken: String): String {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = auth.signInWithCredential(credential).await()
            result.user?.uid ?: throw Exception("No se pudo obtener el UID del usuario autenticado con Google.")
        } catch (e: Exception) {
            throw Exception("Error al iniciar sesión con Google: ${e.message}")
        }
    }

    // Verificar si el usuario está autenticado actualmente
    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    // Cerrar sesión
    fun logout() {
        auth.signOut()
    }

    fun getCurrentUserId(): String {
        return auth.currentUser?.uid ?: ""
    }
}