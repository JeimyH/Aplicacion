package com.example.frontendproyectoapp.viewModel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.frontendproyectoapp.model.UserPreferences
import com.example.frontendproyectoapp.model.Usuario
import com.example.frontendproyectoapp.model.UsuarioEntrada
import com.example.frontendproyectoapp.repository.UsuarioRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    var registroExitoso by mutableStateOf(false)
    var cargando by mutableStateOf(false)
    var errorRegistro by mutableStateOf<String?>(null)

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

    fun registrarUsuario(onResultado: (Boolean) -> Unit) {
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

        repositoryUsuario.registrarUsuario(usuario) { usuarioRespuesta ->
            cargando = false
            registroExitoso = usuarioRespuesta != null

            if (usuarioRespuesta != null) {
                viewModelScope.launch {
                    UserPreferences.guardarIdUsuario(context, usuarioRespuesta.idUsuario)
                    Log.d("UsuarioViewModel", "ID de usuario registrado: ${usuarioRespuesta.idUsuario}")
                    onResultado(true) // Ahora dentro del mismo bloque, después de guardar
                }
            } else {
                errorRegistro = "Error al registrar usuario"
                onResultado(false)
            }
        }
    }
}
