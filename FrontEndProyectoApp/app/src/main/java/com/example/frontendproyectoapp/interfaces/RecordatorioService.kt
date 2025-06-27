package com.example.frontendproyectoapp.interfaces

import com.example.frontendproyectoapp.model.Recordatorio
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

interface RecordatorioService {
    @GET("/api/Recordatorio/listar")
    suspend fun listarRecordatorio(): List<Recordatorio>

    @GET("/api/Recordatorio/buscar/{id_recordatorio}")
    suspend fun listarPorIdRecordatorio(@Path("id_recordatorio") id_recordatorio: Long): Recordatorio

    @POST("/api/Recordatorio/guardar")
    suspend fun guardarRecordatorio(@Body recordatorio: Recordatorio): Recordatorio

    @DELETE("/api/Recordatorio/eliminar/{id_recordatorio}")
    suspend fun eliminarRecordatorio(@Path("id_recordatorio") id_recordatorio: Long)

    @PUT("/api/Recordatorio/actualizar/{id_recordatorio}")
    suspend fun actualizarRecordatorio(
        @Path("id_recordatorio") id_recordatorio: Long,
        @Body recordatorio: Recordatorio
    ): Recordatorio

}

object RetrofitClientRecordatorio {
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

    val recordatorioService: RecordatorioService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)  // Importante: asigna el cliente con el interceptor
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RecordatorioService::class.java)
    }
}