package com.economy.splitpay.repository

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
            val token = firestoreService.generateUniqueGroupToken()
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

            // Crear el grupo y obtener el ID generado
            val groupId = firestoreService.saveGroup(group)

            // Actualizar el usuario actual para agregar el grupo creado a groupsLed
            val user = firestoreService.getUserById(leaderId)
                ?: throw Exception("No se encontró al usuario con ID: $leaderId")

            val updatedGroupsLed = user.groupsLed.toMutableList().apply { add(groupId) }
            firestoreService.updateUser(user.userId, mapOf("groupsLed" to updatedGroupsLed))

            return groupId
        } catch (e: Exception) {
            throw Exception("Error al crear el grupo: ${e.message}")
        }
    }

    // Obtener un grupo por ID
    suspend fun getGroupById(groupId: String): Group? {
        return firestoreService.getGroupById(groupId)
    }

    // Obtener un grupo por token
    suspend fun getGroupByToken(token: String): Group? {
        return firestoreService.getGroupByToken(token)
    }

    // Unirse a un grupo utilizando el token
    suspend fun joinGroupByToken(token: String, userId: String) {
        try {
            val group = firestoreService.getGroupByToken(token)
                ?: throw Exception("No se encontró ningún grupo con el token proporcionado.")

            val updatedMembers = group.members.toMutableList()
            if (updatedMembers.any { it.userId == userId }) {
                throw Exception("El usuario ya es miembro del grupo.")
            }

            val user = firestoreService.getUserById(userId)
                ?: throw Exception("No se encontró al usuario con ID: $userId")

            val updatedGroupsJoined = user.groupsJoined.toMutableList().apply { group.groupId?.let {
                add(
                    it
                )
            } }
            firestoreService.updateUser(user.userId, mapOf("groupsJoined" to updatedGroupsJoined))

            updatedMembers.add(Member(userId = userId))
            firestoreService.updateGroupMembers(group.groupId!!, updatedMembers)
        } catch (e: Exception) {
            throw Exception("Error al unirse al grupo: ${e.message}")
        }
    }

    // Actualizar estado del grupo
    suspend fun updateGroupStatus(groupId: String, status: String) {
        firestoreService.updateGroup(groupId, mapOf("status" to status))
    }

    // Actualizar miembros del grupo
    suspend fun updateGroupMembers(groupId: String, members: List<Member>) {
        firestoreService.updateGroupMembers(groupId, members)
    }
}
