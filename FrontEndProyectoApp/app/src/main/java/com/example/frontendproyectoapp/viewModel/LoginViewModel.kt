package com.example.frontendproyectoapp.viewModel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
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

    var correo by mutableStateOf("")
        private set

    var contrasena by mutableStateOf("")
        private set

    var correoValidationError by mutableStateOf<String?>(null)

    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")

    fun onCorreoChanged(value: String) {
        correo = value
        correoValidationError = validateCorreo(value)
        //Log.d("LoginViewModel", "Correo cambiado: $correo")
    }

    fun onContrasenaChanged(value: String) {
        contrasena = value
        //Log.d("LoginViewModel", "Contraseña cambiada")
    }

    fun validateCorreo(correo: String): String? {
        return when {
            correo.isBlank() -> "El correo no puede estar vacío"
            !emailRegex.matches(correo) -> "Correo no válido"
            else -> null
        }
    }

    fun login(correo: String, contrasena: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            //Log.d("LoginViewModel", "Intentando login con: $correo")

            val result = repositoryUsuario.login(correo, contrasena)

            result.fold(
                onSuccess = { usuario ->
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
                    Log.e("LoginViewModel", "Login fallido: ${it.message}")
                    _uiState.value = LoginUiState.Error(errorMessage)
                }
            )
        }
    }

    fun resetState() {
        _uiState.value = LoginUiState.Idle
        Log.d("LoginViewModel", "Estado del login reseteado")
    }
}
