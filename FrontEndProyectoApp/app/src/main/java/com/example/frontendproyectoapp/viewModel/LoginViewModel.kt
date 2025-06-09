package com.example.frontendproyectoapp.viewModel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontendproyectoapp.model.UserPreferences
import com.example.frontendproyectoapp.model.UsuarioRespuesta
import com.example.frontendproyectoapp.repository.UsuarioRepository
import kotlinx.coroutines.launch

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val usuario: UsuarioRespuesta) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext
    private val repositoryUsuario = UsuarioRepository()

    private val _uiState = mutableStateOf<LoginUiState>(LoginUiState.Idle)
    val uiState: State<LoginUiState> = _uiState

    fun login(correo: String, contrasena: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading

            val result = repositoryUsuario.login(correo, contrasena)

            result.fold(
                onSuccess = { usuario ->
                    // Guardar el ID de forma secuencial y segura
                    UserPreferences.guardarIdUsuario(context, usuario.idUsuario)
                    Log.d("LoginViewModel", "ID de usuario guardado: ${usuario.idUsuario}")
                    _uiState.value = LoginUiState.Success(usuario)
                },
                onFailure = {
                    val errorMessage = when (it.message) {
                        "Correo o contraseña incorrectos" -> "Correo o contraseña incorrectos. Por favor, verifica tus credenciales."
                        "Respuesta vacía" -> "Error en el servidor. Inténtalo de nuevo más tarde."
                        else -> "Ocurrió un error durante el inicio de sesión. Inténtalo de nuevo."
                    }
                    _uiState.value = LoginUiState.Error(errorMessage)
                }
            )
        }
    }


    fun resetState() {
        _uiState.value = LoginUiState.Idle
    }
}