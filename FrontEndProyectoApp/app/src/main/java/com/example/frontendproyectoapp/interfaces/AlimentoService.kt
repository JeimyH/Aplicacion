package com.example.frontendproyectoapp.interfaces

import com.example.frontendproyectoapp.model.Alimento
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AlimentoService {
    @GET("/api/Alimento/listar")
    suspend fun listarAlimentos(): List<Alimento>

    @GET("/api/Alimento/buscar/{idAlimento}")
    suspend fun listarIdAlimento(@Path("idAlimento") idAlimento: Long): Alimento

    @GET("api/Alimento/alimentoCategoria/{categoria}")
    suspend fun obtenerAlimentosPorCategoria(@Path("categoria") categoria: String): List<Alimento>

    @GET("/api/Alimento/favoritos/{idUsuario}")
    suspend fun obtenerFavoritos(@Path("idUsuario") idUsuario: Long): List<Alimento>

    @POST("/api/Alimento/favoritoAgregar/{idUsuario}/{idAlimento}")
    suspend fun marcarFavorito(
        @Path("idUsuario") idUsuario: Long,
        @Path("idAlimento") idAlimento: Long
    )

    @DELETE("/api/Alimento/favoritoEliminar/{idUsuario}/{idAlimento}")
    suspend fun eliminarFavorito(
        @Path("idUsuario") idUsuario: Long,
        @Path("idAlimento") idAlimento: Long
    )

}