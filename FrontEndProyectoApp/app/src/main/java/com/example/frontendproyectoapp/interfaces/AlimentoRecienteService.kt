package com.example.frontendproyectoapp.interfaces

import com.example.frontendproyectoapp.model.AlimentoReciente
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AlimentoRecienteService {

    @POST("api/Reciente/registrar/{idUsuario}/{idAlimento}")
    suspend fun registrarReciente(
        @Path("idUsuario") idUsuario: Long,
        @Path("idAlimento") idAlimento: Long
    ): Response<Void>

    @GET("api/Reciente/consultar/{idUsuario}")
    suspend fun obtenerRecientes(
        @Path("idUsuario") idUsuario: Long
    ): List<AlimentoReciente>

    @DELETE("api/Reciente/eliminarTodos/{idUsuario}")
    suspend fun eliminarTodos(@Path("idUsuario") idUsuario: Long): Response<Unit>

    @DELETE("api/Reciente/eliminar/{idUsuario}/{idAlimento}")
    suspend fun eliminarRecienteIndividual(
        @Path("idUsuario") idUsuario: Long,
        @Path("idAlimento") idAlimento: Long
    ): Response<Unit>

}