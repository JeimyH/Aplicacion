package com.example.frontendproyectoapp.viewModel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.frontendproyectoapp.interfaces.RetrofitClientAlimento
import com.example.frontendproyectoapp.model.Alimento
import com.example.frontendproyectoapp.model.UserPreferences
import com.example.frontendproyectoapp.model.Usuario
import com.example.frontendproyectoapp.model.UsuarioEntrada
import com.example.frontendproyectoapp.repository.UsuarioRepository
import kotlinx.coroutines.joinAll
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
    var confirmarContrasena by mutableStateOf("")
    var fechaNacimiento by mutableStateOf("")
    var altura by mutableStateOf(0f)
    var peso by mutableStateOf(0f)
    var sexo by mutableStateOf("")
    var restriccionesDieta by mutableStateOf("")
    var objetivosSalud by mutableStateOf("")
    var pesoObjetivo by mutableStateOf(0f)

    // Regex de validación
    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
    private val passwordRegex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$")

    // Estados de error
    var nombreValidationError by mutableStateOf<String?>(null)
    var correoValidationError by mutableStateOf<String?>(null)
    var contrasenaValidationError by mutableStateOf<String?>(null)
    var confirmarContrasenaValidationError by mutableStateOf<String?>(null)

    // Estado general
    var registroExitoso by mutableStateOf(false)
    var cargando by mutableStateOf(false)
    var errorRegistro by mutableStateOf<String?>(null)

    //Variable para guardar temporalmente los alimentos hasta que se registre un usuario y se puedan guardar los alimentos
    val alimentosFavoritos = mutableStateListOf<Alimento>()

    fun validateNombre(nombre: String): String? {
        return if (nombre.isBlank()) "El nombre no puede estar vacío" else null
    }

    fun validateEmail(email: String): String? {
        return if (!emailRegex.matches(email)) "Correo inválido" else null
    }

    fun validatePassword(password: String): String? {
        return if (passwordRegex.matches(password)) null
        else "Mínimo 8 caracteres, una mayúscula, una minúscula y un número"
    }

    fun validateConfirmPassword(password: String, confirmPassword: String): String? {
        return if (password == confirmPassword) null
        else "Las contraseñas no coinciden"
    }

    fun toggleFavorito(alimento: Alimento) {
        if (alimentosFavoritos.any { it.idAlimento == alimento.idAlimento }) {
            alimentosFavoritos.removeIf { it.idAlimento == alimento.idAlimento }
        } else {
            alimentosFavoritos.add(alimento)
        }
    }

    fun registrarUsuario(onResultado: (Boolean) -> Unit) {
        viewModelScope.launch {
            cargando = true
            registroExitoso = false
            errorRegistro = null

            // Validaciones locales
            nombreValidationError = validateNombre(nombre)
            correoValidationError = validateEmail(correo)
            contrasenaValidationError = validatePassword(contrasena)
            confirmarContrasenaValidationError = validateConfirmPassword(contrasena, confirmarContrasena)

            if (correoValidationError == null && correo.isNotBlank()) {
                if (repositoryUsuario.existeCorreo(correo)) {
                    correoValidationError = "El correo ya está registrado"
                }
            }

            if (nombreValidationError == null && nombre.isNotBlank()) {
                if (repositoryUsuario.existeNombre(nombre)) {
                    nombreValidationError = "El nombre de usuario ya está en uso"
                }
            }

            if (
                nombreValidationError != null ||
                correoValidationError != null ||
                contrasenaValidationError != null ||
                confirmarContrasenaValidationError != null
            ) {
                cargando = false
                onResultado(false)
                return@launch
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

            val result = repositoryUsuario.registrarUsuario(usuario)

            result.fold(
                onSuccess = { usuarioRespuesta ->
                    registroExitoso = true
                    UserPreferences.guardarIdUsuario(context, usuarioRespuesta.idUsuario)

                    try {
                        kotlinx.coroutines.coroutineScope {
                            alimentosFavoritos.map { alimento ->
                                launch {
                                    try {
                                        RetrofitClientAlimento.alimentoService.marcarFavorito(
                                            usuarioRespuesta.idUsuario,
                                            alimento.idAlimento
                                        )
                                    } catch (e: Exception) {
                                        Log.e("UsuarioViewModel", "Error guardando favorito: ${e.message}")
                                    }
                                }
                            }.joinAll()
                        }
                    } catch (e: Exception) {
                        Log.e("UsuarioViewModel", "Error en favoritos: ${e.message}")
                    }

                    cargando = false
                    onResultado(true)
                },
                onFailure = { throwable ->
                    errorRegistro = throwable.message ?: "Error inesperado al registrar"
                    cargando = false
                    onResultado(false)
                }
            )
        }
    }

}

