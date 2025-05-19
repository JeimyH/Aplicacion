package com.example.frontendproyectoapp.interfaces

import com.example.frontendproyectoapp.model.InteraccionChatbot
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

interface InteraccionChatbotService{
    @GET("/api/InteraccionChatbot/listar")
    suspend fun listarInteraccionChatbot(): List<InteraccionChatbot>

    @GET("/api/InteraccionChatbot/buscar/{id_interaccion}")
    suspend fun listarIdInteraccionChatbot(@Path("id_interaccion") id_interaccion: Long): InteraccionChatbot

    @POST("/api/InteraccionChatbot/guardar")
    suspend fun guardarInteraccionChatbot(@Body interaccionChatbot: InteraccionChatbot): InteraccionChatbot

    @DELETE("/api/InteraccionChatbot/eliminar/{id_interaccion}")
    suspend fun eliminarInteraccionChatbot(@Path("id_interaccion") id_interaccion: Long)

    @PUT("/api/InteraccionChatbot/actualizar/{id_interacion}")
    suspend fun actualizarInteraccionChatbot(@Path("id_interaccion") id_interaccion: Long, @Body interaccionActualizado: InteraccionChatbot): InteraccionChatbot


}

object RetrofitClientInteraccionChatbot {
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

    val interaccionChatbotService: InteraccionChatbotService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)  // Importante: asigna el cliente con el interceptor
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(InteraccionChatbotService::class.java)
    }
}
