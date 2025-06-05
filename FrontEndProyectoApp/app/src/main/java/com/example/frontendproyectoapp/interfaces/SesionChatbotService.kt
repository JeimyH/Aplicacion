package com.example.frontendproyectoapp.interfaces

import com.example.frontendproyectoapp.model.SesionChatbot
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface SesionChatbotService {
    @GET("/api/SesionChatbot/listar")
    suspend fun listarSesionChatbot(): List<SesionChatbot>

    @GET("/api/SesionChatbot/buscar/{id_sesion}")
    suspend fun listarPorIdSesionChatbot(@Path("id_sesion") id_sesion: Long): SesionChatbot

    @POST("/api/SesionChatbot/guardar")
    suspend fun guardarSesionChatbot(@Body sesionChatbot: SesionChatbot): SesionChatbot

    @DELETE("/api/SesionChatbot/eliminar/{id_sesion}")
    suspend fun eliminarSesionChatbot(@Path("id_sesion") id_sesion: Long)

    @PUT("/api/SesionChatbot/actualizar/{id_sesion}")
    suspend fun actualizarSesionChatbot(@Path("id_sesion") id_sesion: Long, @Body sesionChatbot: SesionChatbot): SesionChatbot

}

object RetrofitClientSesionChatbot {
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

    val sesionChatbotService: SesionChatbotService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)  // Importante: asigna el cliente con el interceptor
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SesionChatbotService::class.java)
    }
}