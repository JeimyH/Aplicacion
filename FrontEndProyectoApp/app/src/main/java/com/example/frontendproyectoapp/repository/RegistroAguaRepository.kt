package com.example.frontendproyectoapp.repository

import com.example.frontendproyectoapp.interfaces.RetrofitClientRegistroAgua
import com.example.frontendproyectoapp.model.ActividadDia
import com.example.frontendproyectoapp.model.RegistroAguaEntrada
import com.example.frontendproyectoapp.model.RegistroAguaRespuesta

class RegistroAguaRepository {
    suspend fun registrarAgua(idUsuario: Long, cantidadml: Int): Result<RegistroAguaRespuesta> {
        return try {
            val response = RetrofitClientRegistroAgua.registroAguaService.registrarAgua(idUsuario, RegistroAguaEntrada(cantidadml))
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) } ?: Result.failure(Exception("Sin respuesta"))
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun obtenerRegistroDeHoy(idUsuario: Long): Result<RegistroAguaRespuesta?> {
        return try {
            val response = RetrofitClientRegistroAgua.registroAguaService.obtenerRegistroHoy(idUsuario)
            if (response.isSuccessful) {
                Result.success(response.body())
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun eliminarRegistroDeHoy(idUsuario: Long): Result<Unit> {
        return try {
            val response = RetrofitClientRegistroAgua.registroAguaService.eliminarRegistroDeHoy(idUsuario)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al eliminar: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun obtenerDiasConActividad(idUsuario: Long): Result<List<ActividadDia>> {
        return try {
            val response = RetrofitClientRegistroAgua.registroAguaService.obtenerDiasConActividad(idUsuario)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
