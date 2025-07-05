package com.example.frontendproyectoapp.repository

import android.util.Log
import com.example.frontendproyectoapp.interfaces.RetrofitClientAlimento
import com.example.frontendproyectoapp.interfaces.RetrofitClientAlimentoReciente
import com.example.frontendproyectoapp.interfaces.RetrofitClientRegistroAlimento
import com.example.frontendproyectoapp.model.Alimento
import com.example.frontendproyectoapp.model.AlimentoReciente
import com.example.frontendproyectoapp.model.RegistroAlimentoEntrada
import com.example.frontendproyectoapp.model.RegistroAlimentoSalida
import retrofit2.Response

class BuscarAlimentoRepository {
    private val alimentoService = RetrofitClientAlimento.alimentoService
    private val recienteService = RetrofitClientAlimentoReciente.alimentoRecienteService

    suspend fun obtenerTodos(): List<Alimento> = alimentoService.listarAlimentos()

    suspend fun buscarPorNombre(nombre: String): Alimento? =
        try { alimentoService.obtenerAlimentoPorNombre(nombre) } catch (e: Exception) { null }

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
        RetrofitClientRegistroAlimento.registroAlimentoService.guardarRegistro(registro)
    }

    suspend fun obtenerComidasRecientes(idUsuario: Long): List<RegistroAlimentoSalida> {
        return RetrofitClientRegistroAlimento.registroAlimentoService.obtenerComidasRecientes(idUsuario)
    }

    suspend fun eliminarRegistrosPorFechaYMomento(idUsuario: Long, fecha: String, momento: String): Response<Unit> {
        Log.d("RegistroRepo", "→ Enviando request DELETE con: idUsuario=$idUsuario, fecha=$fecha, momento=$momento")
        return RetrofitClientRegistroAlimento.registroAlimentoService
            .eliminarPorFechaYMomento(idUsuario, momento, fecha) // orden correcto de los @Path
    }


    suspend fun eliminarRegistroPorId(idRegistro: Long) {
        val response = RetrofitClientRegistroAlimento.registroAlimentoService.eliminarRegistroPorId(idRegistro)
        if (!response.isSuccessful) throw Exception("No se pudo eliminar el registro")
    }

}
