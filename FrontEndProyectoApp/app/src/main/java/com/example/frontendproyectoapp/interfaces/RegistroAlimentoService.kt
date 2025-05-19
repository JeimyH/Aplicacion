package com.example.frontendproyectoapp.interfaces

import com.example.frontendproyectoapp.model.RegistroAlimento
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

interface RegistroAlimentoService {
    @GET("/api/RegistroAlimento/listar")
    suspend fun listarRegistroAlimento(): List<RegistroAlimento>

    @GET("/api/RegistroAlimento/buscar/{id_registroAlimento}")
    suspend fun listarPorIdRegistroAlimento(@Path("id_registroAlimento") id_registroAlimento: Long): RegistroAlimento

    @POST("/api/RegistroAlimento/guardar")
    suspend fun guardarRegistroAlimento(@Body registroAlimento: RegistroAlimento): RegistroAlimento

    @DELETE("/api/RegistroAlimento/eliminar/{id_registroAlimento}")
    suspend fun eliminarRegistroAlimento(@Path("id_registroAlimento") id_registroAlimento: Long)

    @PUT("/api/RegistroAlimento/actualizar/{id_registroAlimento}")
    suspend fun actualizarRegistroAlimento(@Path("id_registroAlimento") id_registroAlimento: Long, @Body registroAlimento: RegistroAlimento): RegistroAlimento

}

object RetrofitClientRegistroAlimento {
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

    val registroAlimentoService: RegistroAlimentoService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)  // Importante: asigna el cliente con el interceptor
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RegistroAlimentoService::class.java)
    }
}