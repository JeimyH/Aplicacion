package com.example.frontendproyectoapp.interfaces

import com.example.frontendproyectoapp.model.ActividadDia
import com.example.frontendproyectoapp.model.RegistroAguaEntrada
import com.example.frontendproyectoapp.model.RegistroAguaRespuesta
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RegistroAguaService {

    @POST("/api/RegistroAgua/registrar/{idUsuario}")
    suspend fun registrarAgua(
        @Path("idUsuario") idUsuario: Long,
        @Body registro: RegistroAguaEntrada
    ): Response<RegistroAguaRespuesta>

    @GET("/api/RegistroAgua/obtener/{idUsuario}/hoy")
    suspend fun obtenerRegistroHoy(
        @Path("idUsuario") idUsuario: Long
    ): Response<RegistroAguaRespuesta?>

    @DELETE("/api/RegistroAgua/eliminar/{idUsuario}/hoy")
    suspend fun eliminarRegistroDeHoy(
        @Path("idUsuario") idUsuario: Long
    ): Response<Void>

    @GET("/api/Actividad/dias-con-actividad/{idUsuario}")
    suspend fun obtenerDiasConActividad(@Path("idUsuario") idUsuario: Long): Response<List<ActividadDia>>

}