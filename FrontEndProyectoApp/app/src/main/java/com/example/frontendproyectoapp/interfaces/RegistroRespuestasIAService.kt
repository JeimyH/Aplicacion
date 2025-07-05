package com.example.frontendproyectoapp.interfaces

import com.example.frontendproyectoapp.model.RegistroRespuestasIA
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

interface RegistroRespuestasIAService {
    @GET("/api/RegistroRespuestasIA/listar")
    suspend fun listarRegistroRespuestasIA(): List<RegistroRespuestasIA>

    @GET("/api/RegistroRespuestasIA/buscar/{id_respuestas}")
    suspend fun listarPorIdRegistroRespuestasIA(@Path("id_respuestas") id_respuestas: Long): RegistroRespuestasIA

    @POST("/api/RegistroRespuestasIA/guardar")
    suspend fun guardarRegistroRespuestasIA(@Body registroRespuestasIA: RegistroRespuestasIA): RegistroRespuestasIA

    @DELETE("/api/RegistroRespuestasIA/eliminar/{id_respuestas}")
    suspend fun eliminarRegistroRespuestasIA(@Path("id_respuestas") id_respuestas: Long)

    @PUT("/api/RegistroRespuestasIA/actualizar/{id_respuestas}")
    suspend fun actualizarRegistroRespuestasIA(@Path("id_respuestas") id_respuestas: Long, @Body registroRespuestasIA: RegistroRespuestasIA): RegistroRespuestasIA

}

object RetrofitClientRegistroRespuestasIA {
    // private const val BASE_URL = "http://10.0.2.2:8080"
    private const val BASE_URL = "http://192.168.1.4:8080"
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

    val registroRespuestasIAService: RegistroRespuestasIAService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)  // Importante: asigna el cliente con el interceptor
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RegistroRespuestasIAService::class.java)
    }
}