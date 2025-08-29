package com.example.frontendproyectoapp.repository

import android.util.Log
import com.example.frontendproyectoapp.interfaces.EstadisticasNutricionalesService
import com.example.frontendproyectoapp.interfaces.RegistroAguaService
import com.example.frontendproyectoapp.interfaces.RetrofitClient
import com.example.frontendproyectoapp.model.ActividadDia
import com.example.frontendproyectoapp.model.NutrientesRecomendados
import com.example.frontendproyectoapp.model.NutrientesTotales
import com.example.frontendproyectoapp.model.RegistroAguaEntrada
import com.example.frontendproyectoapp.model.RegistroAguaRespuesta
import java.time.LocalDate

class InicioRepository {

    private val regAguaService = RetrofitClient.createService(RegistroAguaService::class.java)
    private val estadisticasService = RetrofitClient.createService(EstadisticasNutricionalesService::class.java)

    suspend fun registrarAgua(idUsuario: Long, cantidadml: Int): Result<RegistroAguaRespuesta> {
        return try {
            val response = regAguaService.registrarAgua(idUsuario, RegistroAguaEntrada(cantidadml))
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
            val response = regAguaService.obtenerRegistroHoy(idUsuario)
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
            val response = regAguaService.eliminarRegistroDeHoy(idUsuario)
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
            val response = regAguaService.obtenerDiasConActividad(idUsuario)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --- Repository ---
    suspend fun obtenerTotalesPorFecha(idUsuario: Long, fecha: LocalDate): NutrientesTotales {
        Log.d("EstadisticasRepo", "→ Llamando API totales: idUsuario=$idUsuario, fecha=$fecha")
        val resultado = estadisticasService.obtenerTotalesPorFecha(idUsuario, fecha.toString())
        Log.d("EstadisticasRepo", "← Respuesta API totales: $resultado")
        return resultado
    }


    suspend fun obtenerRecomendaciones(idUsuario: Long): Result<NutrientesRecomendados> {
        return try {
            val response = estadisticasService.obtenerRecomendaciones(idUsuario)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Respuesta vacía"))
            } else {
                Result.failure(Exception("Error HTTP: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
