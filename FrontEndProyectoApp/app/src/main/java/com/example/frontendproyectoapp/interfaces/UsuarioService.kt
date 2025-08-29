package com.example.frontendproyectoapp.interfaces

import com.example.frontendproyectoapp.model.Login
import com.example.frontendproyectoapp.model.UsuarioEntrada
import com.example.frontendproyectoapp.model.UsuarioRespuesta
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface UsuarioService {

    @POST("/api/Usuario/login")
    suspend fun login(@Body login: Login): Response<UsuarioRespuesta>

    @POST("api/Usuario/registrar")
    suspend fun registrarUsuario(@Body usuario: UsuarioEntrada): Response<UsuarioRespuesta>

    @GET("/api/Usuario/existeCorreo")
    suspend fun verificarCorreoExistente(@Query("correo") correo: String): Boolean

    @GET("/api/Usuario/existeNombre")
    suspend fun verificarNombreExistente(@Query("nombre") nombre: String): Boolean

    @GET("api/Usuario/{idUsuario}")
    suspend fun obtenerUsuarioPorId(
        @Path("idUsuario") idUsuario: Long
    ): Response<UsuarioRespuesta>

    @DELETE("/api/Usuario/eliminar/{idUsuario}")
    suspend fun eliminarUsuario(@Path("idUsuario") idUsuario: Long): Response<Unit>

    @PUT("/api/Usuario/actualizarAltura/{idUsuario}")
    suspend fun actualizarAltura(
        @Path("idUsuario") idUsuario: Long,
        @Query("alturaActualizada") alturaActualizada: Float
    ): Response<UsuarioRespuesta>

    @PUT("/api/Usuario/actualizarPeso/{idUsuario}")
    suspend fun actualizarPeso(
        @Path("idUsuario") idUsuario: Long,
        @Query("pesoActualizado") pesoActualizado: Float
    ): Response<UsuarioRespuesta>

    @PUT("/api/Usuario/actualizarPesoObjetivo/{idUsuario}")
    suspend fun actualizarPesoObjetivo(
        @Path("idUsuario") idUsuario: Long,
        @Query("pesoObjetivoActualizado") pesoObjetivoActualizado: Float
    ): Response<UsuarioRespuesta>

    @PUT("/api/Usuario/actualizarCorreo/{idUsuario}")
    suspend fun actualizarCorreo(
        @Path("idUsuario") idUsuario: Long,
        @Query("correoActualizado") correoActualizado: String
    ): Response<UsuarioRespuesta>

    @PUT("/api/Usuario/actualizarDieta/{idUsuario}")
    suspend fun actualizarDieta(
        @Path("idUsuario") idUsuario: Long,
        @Query("dietaActualizada") dietaActualizada: String
    ): Response<UsuarioRespuesta>

    @PUT("/api/Usuario/actualizarObjetivo/{idUsuario}")
    suspend fun actualizarObjetivo(
        @Path("idUsuario") idUsuario: Long,
        @Query("objetivoActualizado") objetivoActualizado: String
    ): Response<UsuarioRespuesta>

    @PUT("/api/Usuario/actualizarNivelAct/{idUsuario}")
    suspend fun actualizarNivelAct(
        @Path("idUsuario") idUsuario: Long,
        @Query("nivelActividadActualizado") nivelActividadActualizado: String
    ): Response<UsuarioRespuesta>
}