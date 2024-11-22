package com.economy.splitpay.viewmodel.group

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.economy.splitpay.model.Group
import com.economy.splitpay.model.Member
import com.economy.splitpay.model.User
import com.economy.splitpay.repository.GroupRepository
import com.economy.splitpay.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GroupViewModel(
    private val groupRepository: GroupRepository = GroupRepository(),
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    private val _groupState = MutableStateFlow<GroupState>(GroupState.Idle)
    val groupState: StateFlow<GroupState> get() = _groupState

    fun getAllGroups() {
        _groupState.value = GroupState.Loading
        Log.d("GroupViewModel", "Estado cambiado a Loading")
        viewModelScope.launch {
            try {
                val userGroups = userRepository.getGroupsForCurrentUser()
                Log.d("GroupViewModel", "Grupos cargados: $userGroups")
                _groupState.value = GroupState.UserGroupsLoaded(userGroups)
                Log.d("GroupViewModel", "Estado cambiado a UserGroupsLoaded")
            } catch (e: Exception) {
                _groupState.value = GroupState.Error("Error al obtener los grupos: ${e.message}")
                Log.e("GroupViewModel", "Error al obtener los grupos", e)
            }
        }
    }


    // Crear un nuevo grupo
    fun createGroup(groupName: String, totalAmount: Double, members: List<Member> = emptyList(),navController: NavController) {
        _groupState.value = GroupState.Loading
        viewModelScope.launch {
            try {
                val leaderId = userRepository.getCurrentUserId()
                val groupId = groupRepository.createGroup(leaderId, groupName, totalAmount, members)
                _groupState.value = GroupState.Success("Grupo creado con éxito. ID: $groupId")
                navController.navigate("pending_group_screen/${groupId}")
            } catch (e: Exception) {
                _groupState.value = GroupState.Error("Error al crear el grupo: ${e.message}")
                e.message?.let { Log.e("Error al crear el grupo:", it) }
            }
        }
    }

    fun loadFriends(){
        _groupState.value = GroupState.Loading
        viewModelScope.launch {
            try {
                val userFriends = userRepository.getUserFriendsWithDetails()
                _groupState.value = GroupState.UserFriendsLoaded(userFriends)
            } catch (e: Exception){
                _groupState.value = GroupState.Error("Error al cargar los amigos: ${e.message}")
            }
        }
    }


    // Actualizar estado del grupo
    fun updateGroupStatus(groupId: String, status: String) {
        _groupState.value = GroupState.Loading
        viewModelScope.launch {
            try {
                groupRepository.updateGroupStatus(groupId, status)
                _groupState.value = GroupState.Success("Estado del grupo actualizado a: $status")
            } catch (e: Exception) {
                _groupState.value = GroupState.Error("Error al actualizar el estado del grupo: ${e.message}")
            }
        }
    }

    // Actualizar miembros del grupo
    fun updateGroupMembers(groupId: String, members: List<Member>) {
        _groupState.value = GroupState.Loading
        viewModelScope.launch {
            try {
                groupRepository.updateGroupMembers(groupId, members)
                _groupState.value = GroupState.MembersUpdated(members)
            } catch (e: Exception) {
                _groupState.value = GroupState.Error("Error al actualizar los miembros del grupo: ${e.message}")
            }
        }
    }
}

// Definición del estado del grupo
sealed class GroupState {
    object Idle : GroupState()
    object Loading : GroupState()
    data class Success(val message: String) : GroupState()
    data class Error(val error: String) : GroupState()
    data class GroupLoaded(val group: Group) : GroupState()
    data class MembersUpdated(val members: List<Member>) : GroupState()
    data class UserGroupsLoaded(val userGroups: List<Group>) : GroupState()
    data class UserFriendsLoaded(val userFriends: List<User>) : GroupState()
}

