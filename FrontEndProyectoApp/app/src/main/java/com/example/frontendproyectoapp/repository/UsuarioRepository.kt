package com.example.frontendproyectoapp.repository

import com.example.frontendproyectoapp.interfaces.RetrofitClientUsuario
import com.example.frontendproyectoapp.model.Login
import com.example.frontendproyectoapp.model.Usuario
import com.example.frontendproyectoapp.model.UsuarioEntrada
import com.example.frontendproyectoapp.model.UsuarioRespuesta

class UsuarioRepository {
    suspend fun obtenerUsuarios(): List<Usuario> {
        return RetrofitClientUsuario.usuarioService.listarUsuario()
    }

    suspend fun obtenerIdUsuario(id_usuario: Long): Usuario {
        return RetrofitClientUsuario.usuarioService.listarPorIdUsuario(id_usuario)
    }

    suspend fun guardarUsuario(usuario: Usuario): Usuario {
        return RetrofitClientUsuario.usuarioService.guardarUsuario(usuario)
    }

    suspend fun eliminarUsuario(id_usuario: Long) {
        RetrofitClientUsuario.usuarioService.eliminarUsuario(id_usuario)
    }

    suspend fun actualizarUsuario(id_usuario: Long, usuario: Usuario): Usuario {
        return RetrofitClientUsuario.usuarioService.actualizarUsuario(id_usuario, usuario)
    }

    suspend fun login(correo: String, contrasena: String): Result<UsuarioRespuesta> {
        return try {
            val response = RetrofitClientUsuario.usuarioService.login(Login(correo, contrasena))
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Respuesta vacía"))
            } else if (response.code() == 401) {
                Result.failure(Exception("Correo o contraseña incorrectos"))
            } else {
                Result.failure(Exception("Error inesperado: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun registrarUsuario(usuario: UsuarioEntrada): Result<UsuarioRespuesta> {
        return try {
            val response = RetrofitClientUsuario.usuarioService.registrarUsuario(usuario)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun existeNombre(nombre: String): Boolean {
        return try {
            RetrofitClientUsuario.usuarioService.verificarNombreExistente(nombre)
        } catch (e: Exception) {
            false
        }
    }

    suspend fun existeCorreo(correo: String): Boolean {
        return try {
            RetrofitClientUsuario.usuarioService.verificarCorreoExistente(correo)
        } catch (e: Exception) {
            false
        }
    }
}