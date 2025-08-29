package com.example.frontendproyectoapp.repository

import com.example.frontendproyectoapp.interfaces.AlimentoService
import com.example.frontendproyectoapp.interfaces.RetrofitClient
import com.example.frontendproyectoapp.model.Alimento

class FavoritosRepository {

    private val alimentoService = RetrofitClient.createService(AlimentoService::class.java)

    suspend fun obtenerFavoritos(idUsuario: Long): List<Alimento> {
        return alimentoService.obtenerFavoritos(idUsuario)
    }

    suspend fun marcarFavorito(idUsuario: Long, idAlimento: Long) {
        alimentoService.marcarFavorito(idUsuario, idAlimento)
    }

    suspend fun eliminarFavorito(idUsuario: Long, idAlimento: Long) {
        alimentoService.eliminarFavorito(idUsuario, idAlimento)
    }
}
