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
                    // ✅ Guardar el ID de forma secuencial y segura
                    UserPreferences.guardarIdUsuario(context, usuario.idUsuario)
                    Log.d("LoginViewModel", "ID de usuario guardado: ${usuario.idUsuario}")
                    _uiState.value = LoginUiState.Success(usuario)
                },
                onFailure = {
                    _uiState.value = LoginUiState.Error(it.message ?: "Error desconocido")
                }
            )
        }
    }


    fun resetState() {
        _uiState.value = LoginUiState.Idle
    }
}