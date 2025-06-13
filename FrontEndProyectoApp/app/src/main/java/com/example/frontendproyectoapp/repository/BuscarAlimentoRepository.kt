package com.example.frontendproyectoapp.repository

import com.example.frontendproyectoapp.interfaces.RetrofitClientAlimento
import com.example.frontendproyectoapp.interfaces.RetrofitClientRegistroAlimento
import com.example.frontendproyectoapp.model.Alimento

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

    suspend fun obtenerUrlImagen(nombre: String): String? =
        try {
            alimentoService.obtenerUrlImagenPorNombre(nombre)
        } catch (e: Exception) {
            null
        }

    suspend fun obtenerAlimentosPorCategoria(categoria: String): List<Alimento> =
        alimentoService.obtenerAlimentosPorCategoria(categoria)

}
