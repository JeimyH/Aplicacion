package com.example.frontendproyectoapp.repository

import com.example.frontendproyectoapp.interfaces.RetrofitClient
import com.example.frontendproyectoapp.interfaces.UsuarioService
import com.example.frontendproyectoapp.model.Login
import com.example.frontendproyectoapp.model.UsuarioEntrada
import com.example.frontendproyectoapp.model.UsuarioRespuesta
import retrofit2.HttpException
import java.io.IOException

class UsuarioRepository {

    private val usuarioService = RetrofitClient.createService(UsuarioService::class.java)

    suspend fun login(correo: String, contrasena: String): Result<UsuarioRespuesta> {
        return try {
            val response = usuarioService.login(Login(correo, contrasena))
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
            val response = usuarioService.registrarUsuario(usuario)
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
            usuarioService.verificarNombreExistente(nombre)
        } catch (e: Exception) {
            false
        }
    }

    suspend fun existeCorreo(correo: String): Boolean {
        return try {
            usuarioService.verificarCorreoExistente(correo)
        } catch (e: Exception) {
            false
        }
    }

    suspend fun obtenerUsuarioPorId(idUsuario: Long): UsuarioRespuesta? {
        return try {
            val response = usuarioService.obtenerUsuarioPorId(idUsuario)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun eliminarCuenta(idUsuario: Long): Result<Unit> {
        return try {
            val response = usuarioService.eliminarUsuario(idUsuario)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al eliminar la cuenta: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun actualizarAltura(idUsuario: Long, altura: Float): Result<UsuarioRespuesta> {
        return try {
            val response = usuarioService.actualizarAltura(idUsuario, altura)
            if (response.isSuccessful) {
                val usuarioActualizado = response.body()
                if (usuarioActualizado != null) {
                    Result.success(usuarioActualizado)
                } else {
                    Result.failure(Exception("Respuesta vacía del servidor"))
                }
            } else {
                // Maneja los códigos de error HTTP como 404 y 400
                when (response.code()) {
                    404 -> Result.failure(Exception("Usuario no encontrado (404)"))
                    400 -> Result.failure(Exception("Datos de entrada inválidos (400)"))
                    else -> Result.failure(HttpException(response))
                }
            }
        } catch (e: IOException) {
            // Maneja errores de red
            Result.failure(Exception("Error de conexión: ${e.message}"))
        } catch (e: Exception) {
            // Maneja otros errores
            Result.failure(e)
        }
    }

    suspend fun actualizarPeso(idUsuario: Long, peso: Float): Result<UsuarioRespuesta> {
        return try {
            val response = usuarioService.actualizarPeso(idUsuario, peso)
            if (response.isSuccessful) {
                val usuarioActualizado = response.body()
                if (usuarioActualizado != null) {
                    Result.success(usuarioActualizado)
                } else {
                    Result.failure(Exception("Respuesta vacía del servidor"))
                }
            } else {
                // Maneja los códigos de error HTTP como 404 y 400
                when (response.code()) {
                    404 -> Result.failure(Exception("Usuario no encontrado (404)"))
                    400 -> Result.failure(Exception("Datos de entrada inválidos (400)"))
                    else -> Result.failure(HttpException(response))
                }
            }
        } catch (e: IOException) {
            // Maneja errores de red
            Result.failure(Exception("Error de conexión: ${e.message}"))
        } catch (e: Exception) {
            // Maneja otros errores
            Result.failure(e)
        }
    }

    suspend fun actualizarPesoObjetivo(idUsuario: Long, pesoObjetivo: Float): Result<UsuarioRespuesta> {
        return try {
            val response = usuarioService.actualizarPesoObjetivo(idUsuario, pesoObjetivo)
            if (response.isSuccessful) {
                val usuarioActualizado = response.body()
                if (usuarioActualizado != null) {
                    Result.success(usuarioActualizado)
                } else {
                    Result.failure(Exception("Respuesta vacía del servidor"))
                }
            } else {
                // Maneja los códigos de error HTTP como 404 y 400
                when (response.code()) {
                    404 -> Result.failure(Exception("Usuario no encontrado (404)"))
                    400 -> Result.failure(Exception("Datos de entrada inválidos (400)"))
                    else -> Result.failure(HttpException(response))
                }
            }
        } catch (e: IOException) {
            // Maneja errores de red
            Result.failure(Exception("Error de conexión: ${e.message}"))
        } catch (e: Exception) {
            // Maneja otros errores
            Result.failure(e)
        }
    }

    suspend fun actualizarCorreo(idUsuario: Long, correo: String): Result<UsuarioRespuesta> {
        return try {
            val response = usuarioService.actualizarCorreo(idUsuario, correo)
            if (response.isSuccessful) {
                val usuarioActualizado = response.body()
                if (usuarioActualizado != null) {
                    Result.success(usuarioActualizado)
                } else {
                    Result.failure(Exception("Respuesta vacía del servidor"))
                }
            } else {
                // Maneja los códigos de error HTTP como 404 y 400
                when (response.code()) {
                    404 -> Result.failure(Exception("Usuario no encontrado (404)"))
                    400 -> Result.failure(Exception("Datos de entrada inválidos (400)"))
                    else -> Result.failure(HttpException(response))
                }
            }
        } catch (e: IOException) {
            // Maneja errores de red
            Result.failure(Exception("Error de conexión: ${e.message}"))
        } catch (e: Exception) {
            // Maneja otros errores
            Result.failure(e)
        }
    }

    suspend fun actualizarDieta(idUsuario: Long, dieta: String): Result<UsuarioRespuesta> {
        return try {
            val response = usuarioService.actualizarDieta(idUsuario, dieta)
            if (response.isSuccessful) {
                val usuarioActualizado = response.body()
                if (usuarioActualizado != null) {
                    Result.success(usuarioActualizado)
                } else {
                    Result.failure(Exception("Respuesta vacía del servidor"))
                }
            } else {
                // Maneja los códigos de error HTTP como 404 y 400
                when (response.code()) {
                    404 -> Result.failure(Exception("Usuario no encontrado (404)"))
                    400 -> Result.failure(Exception("Datos de entrada inválidos (400)"))
                    else -> Result.failure(HttpException(response))
                }
            }
        } catch (e: IOException) {
            // Maneja errores de red
            Result.failure(Exception("Error de conexión: ${e.message}"))
        } catch (e: Exception) {
            // Maneja otros errores
            Result.failure(e)
        }
    }

    suspend fun actualizarObjetivo(idUsuario: Long, objetivo: String): Result<UsuarioRespuesta> {
        return try {
            val response = usuarioService.actualizarObjetivo(idUsuario, objetivo)
            if (response.isSuccessful) {
                val usuarioActualizado = response.body()
                if (usuarioActualizado != null) {
                    Result.success(usuarioActualizado)
                } else {
                    Result.failure(Exception("Respuesta vacía del servidor"))
                }
            } else {
                // Maneja los códigos de error HTTP como 404 y 400
                when (response.code()) {
                    404 -> Result.failure(Exception("Usuario no encontrado (404)"))
                    400 -> Result.failure(Exception("Datos de entrada inválidos (400)"))
                    else -> Result.failure(HttpException(response))
                }
            }
        } catch (e: IOException) {
            // Maneja errores de red
            Result.failure(Exception("Error de conexión: ${e.message}"))
        } catch (e: Exception) {
            // Maneja otros errores
            Result.failure(e)
        }
    }

    suspend fun actualizarNivelAct(idUsuario: Long, nivelAct: String): Result<UsuarioRespuesta> {
        return try {
            val response = usuarioService.actualizarNivelAct(idUsuario, nivelAct)
            if (response.isSuccessful) {
                val usuarioActualizado = response.body()
                if (usuarioActualizado != null) {
                    Result.success(usuarioActualizado)
                } else {
                    Result.failure(Exception("Respuesta vacía del servidor"))
                }
            } else {
                // Maneja los códigos de error HTTP como 404 y 400
                when (response.code()) {
                    404 -> Result.failure(Exception("Usuario no encontrado (404)"))
                    400 -> Result.failure(Exception("Datos de entrada inválidos (400)"))
                    else -> Result.failure(HttpException(response))
                }
            }
        } catch (e: IOException) {
            // Maneja errores de red
            Result.failure(Exception("Error de conexión: ${e.message}"))
        } catch (e: Exception) {
            // Maneja otros errores
            Result.failure(e)
        }
    }
}