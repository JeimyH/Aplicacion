package com.example.frontendproyectoapp.interfaces

import com.example.frontendproyectoapp.model.ConfiguracionAplicacion
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

interface ConfiguracionAplicacionService{
    @GET("/api/Configuracion/listar")
    suspend fun listarConfiguracionAplicacion(): List<ConfiguracionAplicacion>

    @GET("/api/Configuracion/buscar/{id_configuracion}")
    suspend fun listarIdConfiguracionAplicacion(@Path("id_configuracion") id_configuracion: Long): ConfiguracionAplicacion

    @POST("/api/Configuracion/guardar")
    suspend fun guardarConfiguracionAplicacion(@Body configuracionAplicacion: ConfiguracionAplicacion): ConfiguracionAplicacion

    @DELETE("/api/Configuracion/eliminar/{id_configuracion}")
    suspend fun eliminarConfiguracionAplicacion(@Path("id_configuracion") id_configuracion: Long)

    @PUT("/api/Configuracion/actualizar/{id_configuracion}")
    suspend fun actualizarConfiguracionAplicacion(@Path("id_configuracion") id_configuracion: Long, @Body configuracionAplicacion: ConfiguracionAplicacion): ConfiguracionAplicacion


}

object RetrofitClientConfiguracionAplicacion {
    // private const val BASE_URL = "http://10.0.2.2:8080"
    private const val BASE_URL = "http://192.168.1.5:8080"
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

    val configuracionAplicacionService: ConfiguracionAplicacionService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)  // Importante: asigna el cliente con el interceptor
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ConfiguracionAplicacionService::class.java)
    }
}