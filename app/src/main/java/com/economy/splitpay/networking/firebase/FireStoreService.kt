package com.economy.splitpay.networking.firebase

import com.economy.splitpay.model.User
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class FirestoreService {
    private val db = FirebaseFirestore.getInstance()

    // Método para obtener un usuario por ID
    suspend fun getUserById(userId: String): User? {
        return try {
            val documentSnapshot: DocumentSnapshot = db.collection("users").document(userId).get().await()
            documentSnapshot.toObject(User::class.java)
        } catch (e: Exception) {
            // Manejar error
            null
        }
    }

    // Método para crear o actualizar un usuario
    suspend fun saveUser(user: User): Boolean {
        return try {
            db.collection("users").document(user.userId).set(user).await()
            true
        } catch (e: Exception) {
            // Manejar error
            false
        }
    }

    // Método para obtener todos los usuarios (opcional)
    suspend fun getAllUsers(): List<User> {
        return try {
            val querySnapshot: QuerySnapshot = db.collection("users").get().await()
            querySnapshot.toObjects(User::class.java)
        } catch (e: Exception) {
            // Manejar error
            emptyList()
        }
    }
}
