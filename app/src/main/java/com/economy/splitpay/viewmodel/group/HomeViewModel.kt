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

class HomeViewModel(
    private val userRepository: UserRepository = UserRepository(),
    private val groupRepository: GroupRepository = GroupRepository()
) : ViewModel() {

    private val _homeState = MutableStateFlow<HomeState>(HomeState.Loading)
    val homeState: StateFlow<HomeState> get() = _homeState

    init {
        loadUserGroups()
    }

    fun loadUserGroups() {
        viewModelScope.launch {
            try {
                val userGroups = userRepository.getGroupsForCurrentUser()
                _homeState.value = HomeState.UserGroupsLoaded(userGroups)
            } catch (e: Exception) {
                _homeState.value = HomeState.Error("Error al cargar los grupos: ${e.message}")
            }
        }
    }

    // Unirse a un grupo por token
    fun joinGroupByToken(token: String) {
        viewModelScope.launch {
            try {
                val userId = userRepository.getCurrentUserId()
                groupRepository.joinGroupByToken(token, userId)
            } catch (e: Exception) {
                _homeState.value = HomeState.Error("Error al unirse al grupo: ${e.message}")
                Log.e("HomeViewModel", "Error al unirse al grupo: ${e.message}")
            }
        }
    }



}

// Estado específico para la HomeScreen
sealed class HomeState {
    object Loading : HomeState()
    data class UserGroupsLoaded(val userGroups: List<Group>) : HomeState()
    data class Error(val message: String) : HomeState()
}
