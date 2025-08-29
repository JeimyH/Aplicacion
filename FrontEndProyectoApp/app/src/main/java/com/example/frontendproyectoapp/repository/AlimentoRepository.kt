package com.example.frontendproyectoapp.repository

import android.util.Log
import com.example.frontendproyectoapp.interfaces.AlimentoRecienteService
import com.example.frontendproyectoapp.interfaces.AlimentoService
import com.example.frontendproyectoapp.interfaces.RegistroAlimentoService
import com.example.frontendproyectoapp.interfaces.RetrofitClient
import com.example.frontendproyectoapp.model.Alimento
import com.example.frontendproyectoapp.model.AlimentoReciente
import com.example.frontendproyectoapp.model.RegistroAlimentoEntrada
import com.example.frontendproyectoapp.model.RegistroAlimentoSalida
import retrofit2.Response

class AlimentoRepository {
    private val alimentoService = RetrofitClient.createService(AlimentoService::class.java)
    private val recienteService = RetrofitClient.createService(AlimentoRecienteService::class.java)
    private val regAlimentoService = RetrofitClient.createService(RegistroAlimentoService::class.java)

    suspend fun obtenerTodos(): List<Alimento> = alimentoService.listarAlimentos()

    suspend fun obtenerFavoritos(idUsuario: Long): List<Alimento> =
        alimentoService.obtenerFavoritos(idUsuario)

    suspend fun obtenerAlimentosPorCategoria(categoria: String): List<Alimento> =
        alimentoService.obtenerAlimentosPorCategoria(categoria)

    suspend fun registrarAlimentoReciente(idUsuario: Long, idAlimento: Long): Boolean {
        val response = recienteService.registrarReciente(idUsuario, idAlimento)
        return response.isSuccessful
    }

    suspend fun obtenerAlimentosRecientes(idUsuario: Long): List<AlimentoReciente> {
        return recienteService.obtenerRecientes(idUsuario)
    }

    suspend fun eliminarTodosRecientes(idUsuario: Long) {
        recienteService.eliminarTodos(idUsuario)
    }

    suspend fun eliminarRecienteIndividual(idUsuario: Long, idAlimento: Long) {
        val response = recienteService.eliminarRecienteIndividual(idUsuario, idAlimento)
        if (!response.isSuccessful) {
            throw Exception("Error al eliminar alimento reciente")
        }
    }

    suspend fun guardarRegistro(registro: RegistroAlimentoEntrada) {
        Log.d("AlimentoRepo", "→ Enviando registro al backend: $registro")
        val response = regAlimentoService.guardarRegistro(registro)
        Log.d("AlimentoRepo", "← Respuesta backend guardarRegistro: ${response.code()} - ${response.message()}")
    }


    suspend fun obtenerComidasRecientes(idUsuario: Long): List<RegistroAlimentoSalida> {
        return regAlimentoService.obtenerComidasRecientes(idUsuario)
    }

    suspend fun eliminarRegistrosPorFechaYMomento(idUsuario: Long, fecha: String, momento: String): Response<Unit> {
        Log.d("RegistroRepo", "→ Enviando request DELETE con: idUsuario=$idUsuario, fecha=$fecha, momento=$momento")
        return regAlimentoService
            .eliminarPorFechaYMomento(idUsuario, momento, fecha) // orden correcto de los @Path
    }

    suspend fun eliminarRegistroPorId(idRegistro: Long) {
        val response = regAlimentoService.eliminarRegistroPorId(idRegistro)
        if (!response.isSuccessful) throw Exception("No se pudo eliminar el registro")
    }

    suspend fun obtenerUnidadesPorId(idAlimento: Long): List<String> {
        val response = regAlimentoService.obtenerUnidadesPorId(idAlimento)
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("Error al obtener unidades por ID: ${response.code()} ${response.message()}")
        }
    }

    suspend fun obtenerUnidadesPorNombre(nombreAlimento: String): List<String> {
        val response = regAlimentoService.obtenerUnidadesPorNombre(nombreAlimento)
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw Exception("Error al obtener unidades por nombre: ${response.code()} ${response.message()}")
        }
    }
}
