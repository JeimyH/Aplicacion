package com.example.frontendproyectoapp.repository

import com.example.frontendproyectoapp.interfaces.RetrofitClientAlimento
import com.example.frontendproyectoapp.interfaces.RetrofitClientRegistroAlimento
import com.example.frontendproyectoapp.model.Alimento
import com.example.frontendproyectoapp.model.RegistroAlimento
import java.time.LocalDate

class BuscarAlimentoRepository {
    private val alimentoService = RetrofitClientAlimento.alimentoService
    private val registroService = RetrofitClientRegistroAlimento.registroAlimentoService

    suspend fun obtenerTodos(): List<Alimento> = alimentoService.listarAlimentos()

    suspend fun buscarPorNombre(nombre: String): Alimento? =
        try { alimentoService.obtenerAlimentoPorNombre(nombre) } catch (e: Exception) { null }

    suspend fun obtenerFavoritos(idUsuario: Long): List<Alimento> =
        alimentoService.obtenerFavoritos(idUsuario)

    suspend fun marcarFavorito(idUsuario: Long, idAlimento: Long) {
        alimentoService.marcarFavorito(idUsuario, idAlimento)
    }

    suspend fun eliminarFavorito(idUsuario: Long, idAlimento: Long) {
        alimentoService.eliminarFavorito(idUsuario, idAlimento)
    }

}
