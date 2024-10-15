package com.economy.splitpay.repository

import com.economy.splitpay.model.User
import com.economy.splitpay.networking.firebase.FirestoreService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(private val firestoreService: FirestoreService) {

    // Obtener usuario por ID
    suspend fun getUserById(userId: String): User? {
        return withContext(Dispatchers.IO) {
            firestoreService.getUserById(userId)
        }
    }

    // Guardar o actualizar un usuario
    suspend fun saveUser(user: User): Boolean {
        return withContext(Dispatchers.IO) {
            firestoreService.saveUser(user)
        }
    }

    // Obtener todos los usuarios (opcional)
    suspend fun getAllUsers(): List<User> {
        return withContext(Dispatchers.IO) {
            firestoreService.getAllUsers()
        }
    }
}