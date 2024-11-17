package com.economy.splitpay.viewmodel.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.economy.splitpay.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    // Estado de registro
    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> get() = _registerState

    // Registrar usuario
    fun registerUser(email: String, password: String, firstname: String,lastname: String, tel: String,username: String) {
        _registerState.value = RegisterState.Loading
        viewModelScope.launch {
            try {
                userRepository.registerUser(email, password, firstname,lastname, tel,username)
                _registerState.value = RegisterState.Success("Usuario registrado exitosamente.")
                Log.d("RegisterViewModel", "Usuario registrado exitosamente.")
            } catch (e: Exception) {
                _registerState.value = RegisterState.Error(e.message ?: "Error desconocido al registrar.")
                Log.e("RegisterViewModel", "Error al registrar usuario: ${e.message}")
            }
        }
    }
}

// Definición del estado de registro
sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    data class Success(val message: String) : RegisterState()
    data class Error(val error: String) : RegisterState()
}