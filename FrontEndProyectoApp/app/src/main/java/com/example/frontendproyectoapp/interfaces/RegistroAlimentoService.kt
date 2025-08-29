package com.example.frontendproyectoapp.interfaces

import com.example.frontendproyectoapp.model.RegistroAlimentoEntrada
import com.example.frontendproyectoapp.model.RegistroAlimentoSalida
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RegistroAlimentoService {
    @POST("/api/RegistroAlimento/registro")
    suspend fun guardarRegistro(@Body registro: RegistroAlimentoEntrada): Response<Unit>

    @GET("/api/RegistroAlimento/recientes/{idUsuario}")
    suspend fun obtenerComidasRecientes(@Path("idUsuario") idUsuario: Long): List<RegistroAlimentoSalida>

    @DELETE("api/RegistroAlimento/eliminar/{idUsuario}/{momento}/{fecha}")
    suspend fun eliminarPorFechaYMomento(
        @Path("idUsuario") idUsuario: Long,
        @Path("momento") momento: String,
        @Path("fecha") fecha: String
    ): Response<Unit>

    @DELETE("/api/RegistroAlimento/eliminar/{idRegistro}")
    suspend fun eliminarRegistroPorId(@Path("idRegistro") idRegistro: Long): Response<Unit>

    @GET("/api/RegistroAlimento/por-id/{idAlimento}")
    suspend fun obtenerUnidadesPorId(
        @Path("idAlimento") idAlimento: Long
    ): Response<List<String>>

    @GET("/api/RegistroAlimento/por-nombre")
    suspend fun obtenerUnidadesPorNombre(
        @Query("nombre") nombreAlimento: String
    ): Response<List<String>>

}