package com.economy.splitpay.viewmodel.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.economy.splitpay.repository.UserRepository

class LoginViewModel(
    private val userRepository: UserRepository = UserRepository()
) : ViewModel() {

    // Estado de login
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> get() = _loginState

    // Iniciar sesión con correo y contraseña
    fun loginUser(email: String, password: String) {
        _loginState.value = LoginState.Loading
        viewModelScope.launch {
            try {
                userRepository.loginUser(email, password)
                _loginState.value = LoginState.Success("Inicio de sesión exitoso.")
                Log.d("LoginViewModel", "Inicio de sesión exitoso.")
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "Error desconocido al iniciar sesión.")
                Log.e("LoginViewModel", "Error al iniciar sesión: ${e.message}")
            }
        }
    }

    // Inicio de sesión con Google
    fun loginWithGoogle(idToken: String) {
        _loginState.value = LoginState.Loading
        viewModelScope.launch {
            try {
                userRepository.loginWithGoogle(idToken)
                _loginState.value = LoginState.Success("Inicio de sesión con Google exitoso.")
                Log.d("LoginViewModel", "Inicio de sesión con Google exitoso.")
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "Error desconocido al iniciar sesión con Google.")
                Log.e("LoginViewModel", "Error al iniciar sesión con Google: ${e.message}")
            }
        }
    }
}

// Definición del estado de login
sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val message: String) : LoginState()
    data class Error(val error: String) : LoginState()
}
