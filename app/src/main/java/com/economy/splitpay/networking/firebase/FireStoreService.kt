package com.economy.splitpay.networking.firebase

import com.economy.splitpay.model.FriendRequest
import com.economy.splitpay.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
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
            val user = document.toObject(User::class.java)
            println("Usuario obtenido para $userId: $user")
            user
        } catch (e: Exception) {
            throw Exception("Error al obtener el usuario de Firestore: ${e.message}")
        }
    }

    // FirestoreService.kt
    suspend fun sendFriendRequest(friendRequest: FriendRequest): String {
        try {
            // Validar que no exista una solicitud duplicada
            val existingRequest = db.collection("friend_requests")
                .whereEqualTo("fromUserId", friendRequest.fromUserId)
                .whereEqualTo("toUserId", friendRequest.toUserId)
                .get()
                .await()

            if (!existingRequest.isEmpty) {
                throw Exception("Ya existe una solicitud de amistad entre estos usuarios.")
            }

            // Guardar la nueva solicitud de amistad
            val documentRef = db.collection("friend_requests").add(friendRequest).await()
            val generatedRequestId = documentRef.id

            documentRef.update("requestId", generatedRequestId).await()
            println("Solicitud de amistad guardada con ID: $generatedRequestId")

            return generatedRequestId // Retorna el ID generado para la solicitud
        } catch (e: Exception) {
            throw Exception("Error al enviar la solicitud de amistad: ${e.message}")
        }
    }


    // Actualizar el estado de una solicitud de amistad
    suspend fun updateFriendRequestStatus(requestId: String, status: String) {
        try {
            db.collection("friend_requests").document(requestId).update("status", status).await()
        } catch (e: Exception) {
            throw Exception("Error al actualizar el estado de la solicitud de amistad: ${e.message}")
        }
    }

    // Obtener todas las solicitudes de amistad de un usuario
    suspend fun getFriendRequests(userId: String): List<FriendRequest> {
        return try {
            val querySnapshot = db.collection("friend_requests")
                .whereEqualTo("toUserId", userId) // Confirma que este campo existe y es correcto
                .get()
                .await()

            val requests = querySnapshot.documents.mapNotNull { it.toObject(FriendRequest::class.java) }
            println("Solicitudes obtenidas de Firestore: $requests")
            requests
        } catch (e: Exception) {
            println("Error al obtener solicitudes de Firestore: ${e.message}")
            emptyList()
        }
    }


    // Buscar usuarios por nombre de usuario
    suspend fun searchUsers(query: String): List<User> {
        return try {
            val querySnapshot = db.collection("users")
                .orderBy("username")
                .startAt(query)
                .endAt(query + "\uf8ff") // Para búsqueda con prefijo
                .get()
                .await()

            querySnapshot.documents.mapNotNull { it.toObject(User::class.java) }
        } catch (e: Exception) {
            throw Exception("Error al buscar usuarios en Firestore: ${e.message}")
        }
    }

    // Observa las solicitudes de amistad en tiempo real
    fun observeFriendRequests(userId: String): Flow<List<FriendRequest>> = callbackFlow {
        val listener = db.collection("friend_requests")
            .whereEqualTo("toUserId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    println("Error al observar solicitudes de amistad: ${error.message}")
                    trySend(emptyList()) // Enviar lista vacía en caso de error
                    return@addSnapshotListener
                }

                val requests = snapshot?.documents?.mapNotNull { it.toObject(FriendRequest::class.java) }
                println("Solicitudes actualizadas desde Firestore: $requests")
                trySend(requests ?: emptyList()) // Enviar la lista actualizada
            }

        // Cerrar el listener cuando se cancele el flujo
        awaitClose { listener.remove() }
    }





}
