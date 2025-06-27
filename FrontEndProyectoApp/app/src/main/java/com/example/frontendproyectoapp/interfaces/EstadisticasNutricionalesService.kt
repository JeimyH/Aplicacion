package com.example.frontendproyectoapp.interfaces

import com.example.frontendproyectoapp.model.EstadisticasNutricionales
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

interface EstadisticasNutricionalesService{
    @GET("/api/EstadisticasNutricionales/listar")
    suspend fun listarEstadisticasNutricionales(): List<EstadisticasNutricionales>

    @GET("/api/EstadisticasNutricionales/buscar/{id_estadisticas}")
    suspend fun listarIdEstadisticasNutricionales(@Path("id_estadisticas") id_estadisticas: Long): EstadisticasNutricionales

    @POST("/api/EstadisticasNutricionales/guardar")
    suspend fun guardarEstadisticasNutricionales(@Body estadisticasNutricionales: EstadisticasNutricionales): EstadisticasNutricionales

    @DELETE("/api/EstadisticasNutricionales/eliminar/{id_estadisticas}")
    suspend fun eliminarEstadisticasNutricionales(@Path("id_estadisticas") id_estadisticas: Long)

    @PUT("/api/EstadisticasNutricionales/actualizar/{id_estadisticas}")
    suspend fun actualizarEstadisticasNutricionales(@Path("id_estadisticas") id_estadisticas: Long, @Body estadisticasActualizado: EstadisticasNutricionales): EstadisticasNutricionales


}

object RetrofitClientEstadisticasNutricionales {
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

    val estadisticasNutricionalesService: EstadisticasNutricionalesService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)  // Importante: asigna el cliente con el interceptor
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EstadisticasNutricionalesService::class.java)
    }
}