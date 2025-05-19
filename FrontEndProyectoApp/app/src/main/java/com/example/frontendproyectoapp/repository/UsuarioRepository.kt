package com.example.frontendproyectoapp.repository

import com.example.frontendproyectoapp.DTO.UsuarioEntradaDTO
import com.example.frontendproyectoapp.interfaces.RetrofitClientUsuario
import com.example.frontendproyectoapp.model.Login
import com.example.frontendproyectoapp.model.Usuario
import com.example.frontendproyectoapp.model.UsuarioEntrada
import com.example.frontendproyectoapp.model.UsuarioRespuesta
import retrofit2.Call

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

    suspend fun loginUsuario(login: Login): UsuarioRespuesta{
        return RetrofitClientUsuario.usuarioService.login(login)
    }

    fun registrarUsuario(usuario: UsuarioEntradaDTO, onResult: (Boolean) -> Unit) {
        val call = RetrofitClientUsuario.usuarioService.registrarUsuario(usuario)
        call.enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: Call<Void>, response: retrofit2.Response<Void>) {
                onResult(response.isSuccessful)
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                onResult(false)
            }
        })
    }
}