package com.example.frontendproyectoapp.interfaces

import com.example.frontendproyectoapp.model.ModificacionRutinaChatbot
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

interface ModificacionRutinaChatbotService{
    @GET("/api/Modificacion/listar")
    suspend fun listarModificacionRutinaChatbot(): List<ModificacionRutinaChatbot>

    @GET("/api/Modificacion/buscar/{id_modificacion}")
    suspend fun listarIdModificacionRutinaChatbot(@Path("id_modificacion") id_modificacion: Long): ModificacionRutinaChatbot

    @POST("/api/Modificacion/guardar")
    suspend fun guardarModificacionRutinaChatbot(@Body modificacionRutinaChatbot: ModificacionRutinaChatbot): ModificacionRutinaChatbot

    @DELETE("/api/Modificacion/eliminar/{id_modificacion}")
    suspend fun eliminarModificacionRutinaChatbot(@Path("id_modificacion") id_modificacion: Long)

    @PUT("/api/Modificacion/actualizar/{id_modificacion}")
    suspend fun actualizarModificacionRutinaChatbot(@Path("id_modificacion") id_modificacion: Long, @Body modificacionActualizado: ModificacionRutinaChatbot): ModificacionRutinaChatbot


}

object RetrofitClientModificacionRutinaChatbot {
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

    val modificacionRutinaChatbotService: ModificacionRutinaChatbotService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)  // Importante: asigna el cliente con el interceptor
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ModificacionRutinaChatbotService::class.java)
    }
}
