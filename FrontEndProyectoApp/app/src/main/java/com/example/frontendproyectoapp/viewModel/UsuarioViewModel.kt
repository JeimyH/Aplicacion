package com.example.frontendproyectoapp.viewModel

import androidx.compose.runtime.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.frontendproyectoapp.model.Usuario
import com.example.frontendproyectoapp.model.UsuarioEntrada
import com.example.frontendproyectoapp.repository.UsuarioRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UsuarioViewModel : ViewModel(){
    private val repositoryUsuario = UsuarioRepository()

    // LiveData inmutable expuesta a la vista
    private val _usuarios = MutableLiveData<List<Usuario>>(emptyList())
    val usuarios: LiveData<List<Usuario>> = _usuarios

    // Estados observables para cada dato del formulario
    // Campos del formulario de registro (reactivos)
    var nombre by mutableStateOf("")
    var correo by mutableStateOf("")
    var contrasena by mutableStateOf("")
    var fechaNacimiento by mutableStateOf("") // formato: yyyy-MM-dd
    var altura by mutableStateOf(0f)
    var peso by mutableStateOf(0f)
    var sexo by mutableStateOf("")
    var restriccionesDieta by mutableStateOf("")
    var objetivosSalud by mutableStateOf("")
    var pesoObjetivo by mutableStateOf(0f)

    // Estado del registro
    var registroExitoso by mutableStateOf(false)
    var cargando by mutableStateOf(false)
    var errorRegistro by mutableStateOf<String?>(null)

    // Obtener todos los usuarios
    fun obtenerUsuarios() {
        viewModelScope.launch(Dispatchers.IO) {
            val usuariosList = repositoryUsuario.obtenerUsuarios()
            _usuarios.postValue(usuariosList)
        }
    }

    // Obtener un usuario por ID (puedes extender para devolverlo como LiveData si lo necesitas)
    fun obtenerUsuario(idUsuario: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val usuario = repositoryUsuario.obtenerIdUsuario(idUsuario)
            // Puedes hacer algo con el usuario si necesitas retornarlo
        }
    }

    // Guardar un nuevo usuario
    fun guardarUsuario(usuario: Usuario) {
        viewModelScope.launch(Dispatchers.IO) {
            repositoryUsuario.guardarUsuario(usuario)
            obtenerUsuarios()
        }
    }

    // Eliminar un usuario por ID
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

        repositoryUsuario.registrarUsuario(usuario) { success ->
            cargando = false
            registroExitoso = success
            if (!success) {
                errorRegistro = "Error al registrar usuario"
            }
            onResultado(success)
        }
    }
}