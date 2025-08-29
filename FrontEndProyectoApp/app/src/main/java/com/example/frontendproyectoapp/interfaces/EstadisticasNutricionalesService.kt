package com.example.frontendproyectoapp.interfaces

import com.example.frontendproyectoapp.model.EstadisticaPorDia
import com.example.frontendproyectoapp.model.EstadisticaPorMes
import com.example.frontendproyectoapp.model.NutrientesRecomendados
import com.example.frontendproyectoapp.model.NutrientesTotales
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EstadisticasNutricionalesService{

    @GET("/api/Estadisticas/totales")
    suspend fun obtenerTotalesPorFecha(
        @Query("idUsuario") idUsuario: Long,
        @Query("fecha") fecha: String // formato yyyy-MM-dd
    ): NutrientesTotales

    @GET("/api/Estadisticas/recomendados")
    suspend fun obtenerRecomendaciones(@Query("idUsuario") idUsuario: Long): Response<NutrientesRecomendados>

    @GET("/api/Estadisticas/recomendados/mensual/{idUsuario}/{anio}/{mes}")
    suspend fun obtenerRecomendacionesMensuales(
        @Path("idUsuario") idUsuario: Long,
        @Path("anio") anio: Int,
        @Path("mes") mes: Int
    ): NutrientesRecomendados

    @GET("/api/Estadisticas/totalDiaria/{idUsuario}/mes")
    suspend fun obtenerConsumoPorDia(
        @Path("idUsuario") idUsuario: Long,
        @Query("anio") anio: Int,
        @Query("mes") mes: Int
    ): List<EstadisticaPorDia>

    @GET("/api/Estadisticas/totalMes/{idUsuario}/anio")
    suspend fun obtenerConsumoPorMes(
        @Path("idUsuario") idUsuario: Long,
        @Query("anio") anio: Int
    ): List<EstadisticaPorMes>
}