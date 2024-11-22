package com.economy.splitpay.networking.firebase

import com.economy.splitpay.model.FriendRequest
import com.economy.splitpay.model.Group
import com.economy.splitpay.model.Member
import com.economy.splitpay.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
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
            val user = document.toObject(User::class.java)
            println("Usuario obtenido para $userId: $user")
            user
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

    // Actualizar el estado de una solicitud de amistad
    suspend fun updateFriendRequestStatus(requestId: String, status: String) {
        try {
            db.collection("friend_requests").document(requestId).update("status", status).await()
        } catch (e: Exception) {
            throw Exception("Error al actualizar el estado de la solicitud de amistad: ${e.message}")
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

    suspend fun addFriend(fromUserId: String, toUserId: String) {
        try {
            // Obtener ambos usuarios
            val fromUser = getUserById(fromUserId)
            val toUser = getUserById(toUserId)

            if (fromUser != null && toUser != null) {
                // Actualizar la lista de amigos de ambos usuarios
                val fromUserFriends = fromUser.friends.toMutableList()
                val toUserFriends = toUser.friends.toMutableList()

                if (!fromUserFriends.contains(toUserId)) {
                    fromUserFriends.add(toUserId)
                }

                if (!toUserFriends.contains(fromUserId)) {
                    toUserFriends.add(fromUserId)
                }

                // Actualizar Firestore
                updateUser(fromUserId, mapOf("friends" to fromUserFriends))
                updateUser(toUserId, mapOf("friends" to toUserFriends))
            } else {
                throw Exception("Uno de los usuarios no existe.")
            }
        } catch (e: Exception) {
            throw Exception("Error al agregar amigo: ${e.message}")
        }
    }






}
