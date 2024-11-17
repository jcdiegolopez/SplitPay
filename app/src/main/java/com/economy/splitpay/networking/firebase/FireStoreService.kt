package com.economy.splitpay.networking.firebase

import com.economy.splitpay.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreService {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Guardar usuario en Firestore
    suspend fun saveUser(user: User) {
        try {
            db.collection("users").document(user.userId).set(user).await()
        } catch (e: Exception) {
            throw Exception("Error al guardar el usuario en Firestore: ${e.message}")
        }
    }

    // Obtener usuario por ID
    suspend fun getUserById(userId: String): User? {
        return try {
            val document = db.collection("users").document(userId).get().await()
            document.toObject(User::class.java) ?: throw Exception("Usuario no encontrado en Firestore.")
        } catch (e: Exception) {
            throw Exception("Error al obtener el usuario de Firestore: ${e.message}")
        }
    }
}
