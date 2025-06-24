package com.example.frontendproyectoapp.repository

import com.example.frontendproyectoapp.interfaces.RetrofitClientAlimento
import com.example.frontendproyectoapp.model.Alimento

class RutinaRepository {
    suspend fun obtenerFavoritos(idUsuario: Long): List<Alimento> {
        return RetrofitClientAlimento.alimentoService.obtenerFavoritos(idUsuario)
    }
/*
    suspend fun obtenerAlimentosPorMomento(
        idUsuario: Long,
        fecha: LocalDate
    ): Map<String, List<Alimento>> {
        return RetrofitClientAlimento.alimentoService.obtenerAlimentosPorMomento(idUsuario, fecha.toString())
    }

 */

    suspend fun marcarFavorito(idUsuario: Long, idAlimento: Long) {
        RetrofitClientAlimento.alimentoService.marcarFavorito(idUsuario, idAlimento)
    }

    suspend fun eliminarFavorito(idUsuario: Long, idAlimento: Long) {
        RetrofitClientAlimento.alimentoService.eliminarFavorito(idUsuario, idAlimento)
    }
}
