package com.economy.splitpay.viewmodel.group


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.economy.splitpay.model.Group
import com.economy.splitpay.repository.GroupRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PendingGroupViewModel(
    private val groupRepository: GroupRepository = GroupRepository()
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
    data class GroupLoaded(val group: Group) : PendingGroupState()
    data class Error(val message: String) : PendingGroupState()
}
