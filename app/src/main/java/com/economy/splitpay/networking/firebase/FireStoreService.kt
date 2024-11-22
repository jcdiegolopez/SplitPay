package com.economy.splitpay.networking.firebase

import com.economy.splitpay.model.User
import com.economy.splitpay.model.FriendRequest
import com.economy.splitpay.model.Group
import com.economy.splitpay.model.Member
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.UUID

class FirestoreService {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    // ---------------------  USUARIOS -----------------------//
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

    suspend fun updateUser(userId: String, updates: Map<String, Any>) {
        try {
            db.collection("users").document(userId).update(updates).await()
        } catch (e: Exception) {
            throw Exception("Error al actualizar el usuario: ${e.message}")
        }
    }



    // -------------------------------  GRUPOS  ----------------------------------//
    // Obtener un grupo por ID
    suspend fun getGroupById(groupId: String): Group? {
        return try {
            val document = db.collection("groups")
                .document(groupId)
                .get()
                .await()
            document.toObject(Group::class.java)?.apply {
                this.groupId = groupId
            }
        } catch (e: Exception) {
            throw Exception("Error al obtener el grupo: ${e.message}")
        }
    }

    // Obtener un grupo por token
    suspend fun getGroupByToken(token: String): Group? {
        return try {
            val querySnapshot = db.collection("groups")
                .whereEqualTo("token", token)
                .get()
                .await()

            if (querySnapshot.isEmpty) {
                null
            } else {
                val document = querySnapshot.documents[0]
                val group = document.toObject(Group::class.java)
                group?.apply { groupId = document.id } // Asignar el ID del documento al objeto Group
            }
        } catch (e: Exception) {
            throw Exception("Error al obtener el grupo por token: ${e.message}")
        }
    }

    // Guardar un nuevo grupo
    suspend fun saveGroup(group: Group): String {
        return try {
            val documentRef = db.collection("groups")
                .add(group)
                .await()
            documentRef.id
        } catch (e: Exception) {
            throw Exception("Error al guardar el grupo: ${e.message}")
        }
    }

    // Actualizar un grupo genérico
    suspend fun updateGroup(groupId: String, updates: Map<String, Any>) {
        try {
            db.collection("groups")
                .document(groupId)
                .update(updates)
                .await()
        } catch (e: Exception) {
            throw Exception("Error al actualizar el grupo: ${e.message}")
        }
    }

    // Actualizar lista de miembros del grupo
    suspend fun updateGroupMembers(groupId: String, members: List<Member>) {
        updateGroup(groupId, mapOf("members" to members))
    }

    // Verificar si un token ya existe
    suspend fun isTokenAlreadyInUse(token: String): Boolean {
        return try {
            val querySnapshot = db.collection("groups")
                .whereEqualTo("token", token)
                .get()
                .await()
            !querySnapshot.isEmpty
        } catch (e: Exception) {
            throw Exception("Error al verificar el token: ${e.message}")
        }
    }

    // Generar un token único para un grupo
    suspend fun generateUniqueGroupToken(): String {
        var token: String
        do {
            token = UUID.randomUUID().toString().take(6)
        } while (isTokenAlreadyInUse(token))
        return token
    }
// ----------------------------  FRIEND REQUEST ---------------------------------//
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
