package com.example.frontendproyectoapp.repository

import com.example.frontendproyectoapp.interfaces.RetrofitClientAlimento
import com.example.frontendproyectoapp.interfaces.RetrofitClientAlimentoReciente
import com.example.frontendproyectoapp.interfaces.RetrofitClientRegistroAlimento
import com.example.frontendproyectoapp.model.Alimento
import com.example.frontendproyectoapp.model.AlimentoReciente
import com.example.frontendproyectoapp.model.RegistroAlimentoEntrada
import com.example.frontendproyectoapp.model.RegistroAlimentoSalida

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

}
