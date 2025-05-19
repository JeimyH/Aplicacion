package com.example.frontendproyectoapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.frontendproyectoapp.model.Usuario
import com.example.frontendproyectoapp.repository.UsuarioRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UsuarioViewModel : ViewModel(){
    private val repositoryUsuario = UsuarioRepository()

    // LiveData inmutable expuesta a la vista
    private val _usuarios = MutableLiveData<List<Usuario>>(emptyList())
    val usuarios: LiveData<List<Usuario>> = _usuarios

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

    // Registrar usuario con parámetros individuales
    fun registrarUsuario(
        nombre: String,
        correo: String,
        contrasena: String,
        fechaNacimiento: String,
        altura: Float,
        peso: Float,
        restriccionesDieta: String,
        objetivosSalud: String
    ) {

        val usuario = Usuario(
            id_usuario = 0L,
            nombre = nombre,
            correo = correo,
            contrasena = contrasena,
            fechaNacimiento = fechaNacimiento,
            altura = altura,
            peso = peso,
            restriccionesDieta = restriccionesDieta,
            objetivosSalud = objetivosSalud
        )
        guardarUsuario(usuario)
    }
}