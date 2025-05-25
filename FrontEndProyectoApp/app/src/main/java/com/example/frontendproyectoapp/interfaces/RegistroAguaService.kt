package com.example.frontendproyectoapp.interfaces

import com.example.frontendproyectoapp.model.RegistroAgua
import com.example.frontendproyectoapp.model.RegistroAguaEntrada
import com.example.frontendproyectoapp.model.RegistroAguaRespuesta
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RegistroAguaService {
    @GET("/api/RegistroAgua/listar")
    suspend fun listarRegistroAgua(): List<RegistroAgua>

    @GET("/api/RegistroAgua/buscar/{id_registroAgua}")
    suspend fun listarPorIdRegistroAgua(@Path("id_registroAgua") id_registroAgua: Long): RegistroAgua

    @POST("/api/agua/registrar/{idUsuario}")
    suspend fun registrarAgua(@Path("idUsuario") idUsuario: Long): Response<Void>

    @DELETE("/api/agua/eliminar/{idUsuario}")
    suspend fun eliminarAgua(@Path("idUsuario") idUsuario: Long): Response<Void>

    @PUT("/api/RegistroAgua/actualizar/{id_registroAgua}")
    suspend fun actualizarRegistroAgua(@Path("id_registroAgua") id_registroAgua: Long, @Body registro: RegistroAgua): RegistroAgua

    @POST("/api/RegistroAgua/{idUsuario}")
    suspend fun registrarAgua(
        @Path("idUsuario") idUsuario: Long,
        @Body registro: RegistroAguaEntrada
    ): Response<RegistroAguaRespuesta>

    @GET("/api/RegistroAgua/{idUsuario}/hoy")
    suspend fun obtenerRegistroHoy(
        @Path("idUsuario") idUsuario: Long
    ): Response<RegistroAguaRespuesta?>
}

object RetrofitClientRegistroAgua {
    private const val BASE_URL = "http://10.0.2.2:8080"
    private const val USUARIO = "admin"  // Cambia aquí por tu usuario
    private const val PASSWORD = "admin123" // Cambia aquí por tu contraseña

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .header("Authorization", Credentials.basic(USUARIO, PASSWORD))
                .method(original.method, original.body)
            val request = requestBuilder.build()
            chain.proceed(request)
        }
        .build()

    val registroAguaService: RegistroAguaService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)  // Importante: asigna el cliente con el interceptor
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RegistroAguaService::class.java)
    }
}