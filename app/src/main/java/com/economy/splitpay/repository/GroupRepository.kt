package com.economy.splitpay.repository

import android.util.Log
import com.economy.splitpay.model.Group
import com.economy.splitpay.model.Member
import com.economy.splitpay.networking.firebase.FirestoreService

class GroupRepository(
    private val firestoreService: FirestoreService = FirestoreService()
) {

    // Crear un nuevo grupo
    suspend fun createGroup(
        leaderId: String,
        groupName: String,
        totalAmount: Double,
        members: List<Member> = emptyList()
    ): String {
        try {
            // Generar un token único para el grupo
            val token = firestoreService.generateUniqueGroupToken()

            // Crear el grupo con los datos iniciales
            val group = Group(
                groupId = null, // Firestore generará el ID automáticamente
                leaderId = leaderId,
                groupName = groupName,
                token = token,
                totalAmount = totalAmount,
                creationDate = System.currentTimeMillis().toString(), // Fecha actual
                status = "pending",
                members = members
            )

            // Guardar el grupo y obtener el ID generado
            val groupId = firestoreService.saveGroup(group)

            // Actualizar el usuario líder para agregar el grupo a su lista de grupos liderados
            val leader = firestoreService.getUserById(leaderId)
                ?: throw Exception("No se encontró al usuario líder con ID: $leaderId")

            val updatedGroupsLed = leader.groupsLed.toMutableList().apply { add(groupId) }
            firestoreService.updateUser(leader.userId, mapOf("groupsLed" to updatedGroupsLed))

            // Actualizar la lista de gruposJoined para cada miembro
            members.forEach { member ->
                val user = firestoreService.getUserById(member.userId)
                    ?: throw Exception("No se encontró al miembro con ID: ${member.userId}")

                val updatedGroupsJoined = user.groupsJoined.toMutableList().apply { add(groupId) }
                firestoreService.updateUser(user.userId, mapOf("groupsJoined" to updatedGroupsJoined))
            }

            return groupId
        } catch (e: Exception) {
            throw Exception("Error al crear el grupo: ${e.message}")
        }
    }


    // Obtener un grupo por ID
    suspend fun getGroupById(groupId: String): Group? {
        return firestoreService.getGroupById(groupId)
    }


    // Unirse a un grupo utilizando el token
    suspend fun joinGroupByToken(token: String, userId: String) {
        try {
            // Obtener el grupo con el token proporcionado
            val group = firestoreService.getGroupByToken(token)
                ?: throw Exception("No se encontró ningún grupo con el token proporcionado.")

            // Verificar que el groupId no sea nulo
            val groupId = group.groupId
                ?: throw Exception("El grupo no tiene un ID válido.")

            // Obtener los miembros actuales del grupo
            val members = group.members ?: listOf() // Manejo de un posible valor nulo
            val updatedMembers = members.toMutableList()

            // Verificar si el usuario ya es miembro del grupo
            if (updatedMembers.any { it.userId == userId }) {
                throw Exception("El usuario ya es miembro del grupo.")
            }

            if (group.leaderId == userId ){
                throw Exception("El usuario ya es lider del grupo.")
            }

            // Obtener la información del usuario
            val user = firestoreService.getUserById(userId)
                ?: throw Exception("No se encontró al usuario con ID: $userId")

            // Actualizar la lista de grupos a los que pertenece el usuario
            val updatedGroupsJoined = user.groupsJoined?.toMutableList() ?: mutableListOf()
            updatedGroupsJoined.add(groupId)
            firestoreService.updateUser(user.userId, mapOf("groupsJoined" to updatedGroupsJoined))

            // Agregar al usuario como miembro del grupo
            updatedMembers.add(Member(userId = userId, assignedAmount = 0.0, name = "${user.firstname} ${user.lastname}" ))
            firestoreService.updateGroupMembers(groupId, updatedMembers)
        } catch (e: Exception) {
            Log.e("GroupRepository", "Error al unirse al grupo:", e)
            throw Exception(" ${e.message}")
        }
    }


    // Actualizar estado del grupo
    suspend fun updateGroupStatus(groupId: String, status: String) {
        firestoreService.updateGroup(groupId, mapOf("status" to status))
    }

    suspend fun updateMemberAcceptance(groupId: String, memberId: String, accepted: Boolean) {
        try {
            // Obtener el grupo actual
            val group = firestoreService.getGroupById(groupId)
                ?: throw Exception("No se encontró el grupo con ID: $groupId")

            // Actualizar el estado del miembro
            val updatedMembers = group.members.map { member ->
                if (member.userId == memberId) {
                    member.copy(accepted = accepted)
                } else {
                    member
                }
            }

            // Guardar los cambios en Firestore
            firestoreService.updateGroupMembers(groupId, updatedMembers)
        } catch (e: Exception) {
            Log.e("GroupRepository", "Error al actualizar aceptación del miembro:", e)
            throw Exception("Error al actualizar aceptación: ${e.message}")
        }
    }


    // Actualizar miembros del grupo
    suspend fun updateGroupMembers(groupId: String, members: List<Member>) {
        firestoreService.updateGroupMembers(groupId, members)
    }
}
