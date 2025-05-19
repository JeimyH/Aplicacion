package com.example.frontendproyectoapp.interfaces

import com.example.frontendproyectoapp.model.ComidaRutinaIA
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

interface ComidaRutinaIAService{
    @GET("/api/ComidaRutinaIA/listar")
    suspend fun listarComidaRutinaIA(): List<ComidaRutinaIA>

    @GET("/api/ComidaRutinaIA/buscar/{id_comida}")
    suspend fun listarIdComidaRutinaIA(@Path("id_comida") id_comida: Long): ComidaRutinaIA

    @POST("/api/ComidaRutinaIA/guardar")
    suspend fun guardarComidaRutinaIA(@Body comidaRutinaIA: ComidaRutinaIA): ComidaRutinaIA

    @DELETE("/api/ComidaRutinaIA/eliminar/{id_comida}")
    suspend fun eliminarComidaRutinaIA(@Path("id_comida") id_comida: Long)

    @PUT("/api/ComidaRutinaIA/actualizar/{id_comida}")
    suspend fun actualizarComidaRutinaIA(@Path("id_comida") id_comida: Long, @Body comidaActualizado: ComidaRutinaIA): ComidaRutinaIA


}

object RetrofitClientComidaRutinaIA {
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

    val comidaRutinaIAService: ComidaRutinaIAService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)  // Importante: asigna el cliente con el interceptor
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ComidaRutinaIAService::class.java)
    }
}


