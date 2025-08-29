package com.example.frontendproyectoapp.repository

import com.example.frontendproyectoapp.interfaces.EstadisticasNutricionalesService
import com.example.frontendproyectoapp.interfaces.RetrofitClient

class EstadisticasRepository {

    private val estadisticasService = RetrofitClient.createService(EstadisticasNutricionalesService::class.java)

    suspend fun getConsumoPorDia(idUsuario: Long, anio: Int, mes: Int) =
        estadisticasService.obtenerConsumoPorDia(idUsuario, anio, mes)

    suspend fun getConsumoPorMes(idUsuario: Long, anio: Int) =
        estadisticasService.obtenerConsumoPorMes(idUsuario, anio)

    suspend fun getRecomendacionesMensuales(idUsuario: Long, anio: Int, mes: Int) =
        estadisticasService.obtenerRecomendacionesMensuales(idUsuario, anio, mes)

}