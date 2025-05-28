package com.example.frontendproyectoapp.repository

import com.example.frontendproyectoapp.interfaces.RetrofitClientUsuario
import com.example.frontendproyectoapp.model.Login
import com.example.frontendproyectoapp.model.Usuario
import com.example.frontendproyectoapp.model.UsuarioEntrada
import com.example.frontendproyectoapp.model.UsuarioRespuesta
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

    /*
    suspend fun loginUsuario(login: Login): UsuarioRespuesta{
            return RetrofitClientUsuario.usuarioService.login(login)
    }
     */
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

    fun registrarUsuario(usuario: UsuarioEntrada, onResultado: (UsuarioRespuesta?) -> Unit) {
        val call = RetrofitClientUsuario.usuarioService.registrarUsuario(usuario)
        call.enqueue(object : Callback<UsuarioRespuesta> {
            override fun onResponse(call: Call<UsuarioRespuesta>, response: Response<UsuarioRespuesta>) {
                if (response.isSuccessful) {
                    onResultado(response.body()) // ✅ Retorna el objeto completo
                } else {
                    onResultado(null)
                }
            }

            override fun onFailure(call: Call<UsuarioRespuesta>, t: Throwable) {
                onResultado(null)
            }
        })
    }

}