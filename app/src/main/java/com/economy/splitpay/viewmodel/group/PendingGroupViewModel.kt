package com.economy.splitpay.viewmodel.group


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.economy.splitpay.model.Group
import com.economy.splitpay.repository.GroupRepository
import com.economy.splitpay.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PendingGroupViewModel(
    private val groupRepository: GroupRepository = GroupRepository(),
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    private val _groupState = MutableStateFlow<PendingGroupState>(PendingGroupState.Loading)
    val groupState: StateFlow<PendingGroupState> get() = _groupState


    // Cargar datos de un grupo por su ID
    fun getGroupById(groupId: String) {
        viewModelScope.launch {
            try {
                val group = groupRepository.getGroupById(groupId)
                if (group != null) {
                    _groupState.value = PendingGroupState.GroupLoaded(group)
                } else {
                    _groupState.value = PendingGroupState.Error("No se encontró el grupo.")
                }
            } catch (e: Exception) {
                _groupState.value = PendingGroupState.Error("Error al cargar el grupo: ${e.message}")
                Log.e("PendingGroupViewModel", "Error al cargar el grupo", e)
            }
        }
    }

    fun loadGroupWithLeaderStatus(groupId: String) {
        viewModelScope.launch {
            try {
                // Obtener el grupo por ID
                val group = groupRepository.getGroupById(groupId)
                    ?: throw Exception("No se encontró el grupo.")

                // Obtener el usuario actual
                val currentUserId = userRepository.getCurrentUserId()
                    ?: throw Exception("No se pudo obtener el ID del usuario actual.")

                // Determinar si el usuario actual es líder
                val isLeader = group.leaderId == currentUserId

                // Actualizar el estado con el grupo y el estado de liderazgo
                _groupState.value = PendingGroupState.GroupLoaded(group, isLeader)
            } catch (e: Exception) {
                _groupState.value = PendingGroupState.Error("Error al cargar el grupo: ${e.message}")
                Log.e("PendingGroupViewModel", "Error al cargar el grupo", e)
            }
        }
    }

    fun updateMemberAcceptance(groupId: String, accepted: Boolean) {
        viewModelScope.launch {
            try {
                groupRepository.updateMemberAcceptance(groupId, userRepository.getCurrentUserId() , accepted)
                loadGroupWithLeaderStatus(groupId) // Recargar los datos del grupo
            } catch (e: Exception) {
                _groupState.value = PendingGroupState.Error("Error al actualizar el estado: ${e.message}")
            }
        }
    }


    // Actualizar el monto asignado a un miembro
    fun updateMemberAmount(groupId: String, memberId: String, newAmount: Double) {
        viewModelScope.launch {
            try {
                val currentGroup = (groupState.value as? PendingGroupState.GroupLoaded)?.group
                    ?: throw Exception("El grupo no está cargado.")

                val updatedMembers = currentGroup.members.map { member ->
                    if (member.userId == memberId) {
                        member.copy(assignedAmount = newAmount)
                    } else {
                        member
                    }
                }

                groupRepository.updateGroupMembers(groupId, updatedMembers)
                _groupState.value = PendingGroupState.GroupLoaded(
                    currentGroup.copy(members = updatedMembers)
                )
            } catch (e: Exception) {
                _groupState.value = PendingGroupState.Error("Error al actualizar el monto: ${e.message}")
            }
        }
    }
}

// Representa el estado de la pantalla de PendingGroup
sealed class PendingGroupState {
    object Loading : PendingGroupState()
    data class GroupLoaded(val group: Group, val isLeader: Boolean = false) : PendingGroupState()
    data class Error(val message: String) : PendingGroupState()
}
