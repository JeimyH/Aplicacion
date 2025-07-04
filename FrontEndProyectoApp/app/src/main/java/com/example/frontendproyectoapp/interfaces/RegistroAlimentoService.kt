package com.example.frontendproyectoapp.interfaces

import com.example.frontendproyectoapp.model.RegistroAlimentoEntrada
import com.example.frontendproyectoapp.model.RegistroAlimentoSalida
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RegistroAlimentoService {
    @POST("/api/RegistroAlimento/registro")
    suspend fun guardarRegistro(@Body registro: RegistroAlimentoEntrada)

    @GET("/api/RegistroAlimento/recientes/{idUsuario}")
    suspend fun obtenerComidasRecientes(@Path("idUsuario") idUsuario: Long): List<RegistroAlimentoSalida>

    @DELETE("api/RegistroAlimento/eliminar/{idUsuario}/{momento}/{fecha}")
    suspend fun eliminarPorFechaYMomento(
        @Path("idUsuario") idUsuario: Long,
        @Path("momento") momento: String,
        @Path("fecha") fecha: String
    ): Response<Unit>

    @DELETE("/api/RegistroAlimento/eliminar/{idRegistro}")
    suspend fun eliminarRegistroPorId(@Path("idRegistro") idRegistro: Long): Response<Unit>

}

object RetrofitClientRegistroAlimento {
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

    val registroAlimentoService: RegistroAlimentoService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)  // Importante: asigna el cliente con el interceptor
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RegistroAlimentoService::class.java)
    }
}