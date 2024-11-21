package com.economy.splitpay.networking.firebase

import com.economy.splitpay.model.User
import com.economy.splitpay.model.FriendRequest
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

    // Enviar una solicitud de amistad
    suspend fun sendFriendRequest(friendRequest: FriendRequest) {
        try {
            db.collection("friend_requests")
                .add(friendRequest)
                .await()
        } catch (e: Exception) {
            throw Exception("Error al enviar la solicitud de amistad: ${e.message}")
        }
    }

    // Obtener solicitudes de amistad recibidas
    suspend fun getReceivedFriendRequests(userId: String): List<FriendRequest> {
        return try {
            db.collection("friend_requests")
                .whereEqualTo("toUserId", userId)
                .get()
                .await()
                .toObjects(FriendRequest::class.java)
        } catch (e: Exception) {
            throw Exception("Error al obtener las solicitudes de amistad: ${e.message}")
        }
    }

    // Actualizar el estado de una solicitud de amistad
    suspend fun updateFriendRequestStatus(requestId: String, status: String) {
        try {
            db.collection("friend_requests")
                .document(requestId)
                .update("status", status)
                .await()
        } catch (e: Exception) {
            throw Exception("Error al actualizar el estado de la solicitud de amistad: ${e.message}")
        }
    }



}
