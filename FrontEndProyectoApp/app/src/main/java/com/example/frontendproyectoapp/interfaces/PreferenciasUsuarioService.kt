package com.example.frontendproyectoapp.interfaces

import com.example.frontendproyectoapp.model.PreferenciasUsuario
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

interface PreferenciasUsuarioService{
    @GET("/api/Preferencias/listar")
    suspend fun listarPreferenciasUsuario(): List<PreferenciasUsuario>

    @GET("/api/Preferencias/buscar/{id_preferencia}")
    suspend fun listarIdPreferenciasUsuario(@Path("id_preferencia") id_preferencia: Long): PreferenciasUsuario

    @POST("/api/Preferencias/guardar")
    suspend fun guardarPreferenciasUsuario(@Body preferenciasUsuario: PreferenciasUsuario): PreferenciasUsuario

    @DELETE("/api/Preferencias/eliminar/{id_preferencia}")
    suspend fun eliminarPreferenciasUsuario(@Path("id_preferencia") id_preferencia: Long)

    @PUT("/api/Preferencias/actualizar/{id_preferencia}")
    suspend fun actualizarPreferenciasUsuario(@Path("id_preferencia") id_preferencia: Long, @Body preferenciasActualizado: PreferenciasUsuario): PreferenciasUsuario


}

object RetrofitClientPreferenciasUsuario {
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

    val preferenciasUsuarioService: PreferenciasUsuarioService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)  // Importante: asigna el cliente con el interceptor
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PreferenciasUsuarioService::class.java)
    }
}
