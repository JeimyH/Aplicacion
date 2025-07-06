package com.example.frontendproyectoapp.interfaces

import com.example.frontendproyectoapp.model.TokenSesion
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

interface TokenSesionService {
    @GET("/api/TokenSesion/listar")
    suspend fun listarTokenSesion(): List<TokenSesion>

    @GET("/api/TokenSesion/buscar/{id_token}")
    suspend fun listarPorIdTokenSesion(@Path("id_token") id_token: Long): TokenSesion

    @POST("/api/TokenSesion/guardar")
    suspend fun guardarTokenSesion(@Body tokenSesion: TokenSesion): TokenSesion

    @DELETE("/api/TokenSesion/eliminar/{id_token}")
    suspend fun eliminarTokenSesion(@Path("id_token") id_token: Long)

    @PUT("/api/TokenSesion/actualizar/{id_token}")
    suspend fun actualizarTokenSesion(@Path("id_token") id_token: Long, @Body tokenSesion: TokenSesion): TokenSesion

}

object RetrofitClientTokenSesion {
    // private const val BASE_URL = "http://10.0.2.2:8080"
    private const val BASE_URL = "http://192.168.1.8:8080"
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

    val tokenSesionService: TokenSesionService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)  // Importante: asigna el cliente con el interceptor
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TokenSesionService::class.java)
    }
}