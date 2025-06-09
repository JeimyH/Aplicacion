package com.example.frontendproyectoapp.viewModel

import android.app.Application
import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.frontendproyectoapp.model.UserPreferences
import com.example.frontendproyectoapp.model.Usuario
import com.example.frontendproyectoapp.model.UsuarioEntrada
import com.example.frontendproyectoapp.repository.UsuarioRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UsuarioViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext
    private val repositoryUsuario = UsuarioRepository()

    // LiveData
    private val _usuarios = MutableLiveData<List<Usuario>>(emptyList())
    val usuarios: LiveData<List<Usuario>> = _usuarios

    // Estados del formulario
    var nombre by mutableStateOf("")
    var correo by mutableStateOf("")
    var contrasena by mutableStateOf("")
    var fechaNacimiento by mutableStateOf("")
    var altura by mutableStateOf(0f)
    var peso by mutableStateOf(0f)
    var sexo by mutableStateOf("")
    var restriccionesDieta by mutableStateOf("")
    var objetivosSalud by mutableStateOf("")
    var pesoObjetivo by mutableStateOf(0f)

    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
    private val passwordRegex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$")

    // Validation error states
    var correoValidationError: String? by mutableStateOf(null)
    var contrasenaValidationError by mutableStateOf<String?>(null)
    var confirmarContrasenaValidationError by mutableStateOf<String?>(null)

    var registroExitoso by mutableStateOf(false)
    var cargando by mutableStateOf(false)
    var errorRegistro by mutableStateOf<String?>(null)
    var nombreValidationError: String? by mutableStateOf(null)


    fun obtenerUsuarios() {
        viewModelScope.launch(Dispatchers.IO) {
            val usuariosList = repositoryUsuario.obtenerUsuarios()
            _usuarios.postValue(usuariosList)
        }
    }

    fun obtenerUsuario(idUsuario: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryUsuario.obtenerIdUsuario(idUsuario)
        }
    }

    fun guardarUsuario(usuario: Usuario) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryUsuario.guardarUsuario(usuario)
            obtenerUsuarios()
        }
    }

    fun eliminarUsuario(idUsuario: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryUsuario.eliminarUsuario(idUsuario)
            obtenerUsuarios()
        }
    }


    fun validateNombre(nombre: String): String? {
        return if (nombre.isBlank()) "El nombre no puede estar vacío"
        else null
    }

    fun validateEmail(email: String): String? {
        return if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            "Correo inválido" else null
    }

    fun verificarNombreExistente(nombre: String) {
        viewModelScope.launch {
            if (nombre.isBlank()) return@launch
            val existe = repositoryUsuario.verificarNombre(nombre) // true/false
            if (existe) {
                nombreValidationError = "Este nombre de usuario ya está registrado"
            } else if (nombreValidationError == "Este nombre de usuario ya está registrado") {
                nombreValidationError = null
            }
        }
    }

    fun verificarCorreoExistente(correo: String) {
        viewModelScope.launch {
            if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) return@launch
            val existe = repositoryUsuario.verificarCorreo(correo)
            if (existe) {
                correoValidationError = "Este correo ya está registrado"
            } else if (correoValidationError == "Este correo ya está registrado") {
                correoValidationError = null
            }
        }
    }

    fun verificarUsuarioExistente(nombre: String, correo: String): String? {
        val listaUsuarios = usuarios.value ?: emptyList()

        return when {
            listaUsuarios.any { it.nombre.equals(nombre, ignoreCase = true) } -> "El nombre ya está registrado"
            listaUsuarios.any { it.correo.equals(correo, ignoreCase = true) } -> "El correo electrónico ya está registrado"
            else -> null
        }
    }

    fun validatePassword(password: String): String? {
        return if (passwordRegex.matches(password)) {
            null // No error
        } else {
            "Mínimo 8 caracteres, una mayúscula, una minúscula y un número"
        }
    }

    fun validateConfirmPassword(password: String, confirmPassword: String): String? {
        return if (password == confirmPassword) {
            null // No error
        } else {
            "Las contraseñas no coinciden"
        }
    }

    fun registrarUsuario(onResultado: (Boolean) -> Unit) {
        // Validaciones locales
        nombreValidationError = validateNombre(nombre)
        correoValidationError = validateEmail(correo)
        contrasenaValidationError = validatePassword(contrasena)

        val usuarioExistenteError = verificarUsuarioExistente(nombre, correo)

        if (nombreValidationError != null || correoValidationError != null || contrasenaValidationError != null || usuarioExistenteError != null) {
            errorRegistro = usuarioExistenteError
            onResultado(false)
            return
        }

        val usuario = UsuarioEntrada(
            nombre = nombre,
            correo = correo,
            contrasena = contrasena,
            fechaNacimiento = fechaNacimiento,
            altura = altura,
            peso = peso,
            sexo = sexo,
            restriccionesDieta = restriccionesDieta,
            objetivosSalud = objetivosSalud,
            pesoObjetivo = pesoObjetivo
        )

        cargando = true
        errorRegistro = null

        viewModelScope.launch {
            val result = repositoryUsuario.registrarUsuario(usuario)

            cargando = false

            result.fold(
                onSuccess = { usuarioRespuesta ->
                    registroExitoso = true
                    UserPreferences.guardarIdUsuario(context, usuarioRespuesta.idUsuario)
                    Log.d("UsuarioViewModel", "ID de usuario registrado: ${usuarioRespuesta.idUsuario}")
                    onResultado(true)
                },
                onFailure = { throwable ->
                    registroExitoso = false
                    val errorMessage = when (throwable.message) {
                        "El correo ya está registrado" -> "El correo electrónico ya se encuentra registrado."
                        "Respuesta vacía" -> "Error en el servidor durante el registro. Inténtalo de nuevo más tarde."
                        else -> "Ocurrió un error durante el registro. Inténtalo de nuevo."
                    }
                    errorRegistro = errorMessage
                    Log.e("UsuarioViewModel", "Error al registrar usuario: ${throwable.message}", throwable)
                    onResultado(false)
                }
            )
        }
    }

}
