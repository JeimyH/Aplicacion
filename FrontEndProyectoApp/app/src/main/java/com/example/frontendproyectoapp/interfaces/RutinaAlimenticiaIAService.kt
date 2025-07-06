package com.example.frontendproyectoapp.interfaces

import com.example.frontendproyectoapp.model.RutinaAlimenticiaIA
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

interface RutinaAlimenticiciaIAService {
    @GET("/api/RutinaIA/listar")
    suspend fun listarRutinaAlimenticiciaIA(): List<RutinaAlimenticiaIA>

    @GET("/api/RutinaIA/buscar/{id_rutina}")
    suspend fun listarPorIdRutinaAlimenticiciaIA(@Path("id_rutina") id_rutina: Long): RutinaAlimenticiaIA

    @POST("/api/RutinaIA/guardar")
    suspend fun guardarRutinaAlimenticiciaIA(@Body rutinaAlimenticiaIA: RutinaAlimenticiaIA): RutinaAlimenticiaIA

    @DELETE("/api/RutinaIA/eliminar/{id_rutina}")
    suspend fun eliminarRutinaAlimenticiciaIA(@Path("id_rutina") id_rutina: Long)

    @PUT("/api/RutinaIA/actualizar/{id_rutina}")
    suspend fun actualizarRutinaAlimenticiciaIA(@Path("id_rutina") id_rutina: Long, @Body rutinaAlimenticiaIA: RutinaAlimenticiaIA): RutinaAlimenticiaIA

}

object RetrofitClientRutinaAlimenticiciaIA {
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

    val rutinaAlimenticiciaIAService: RutinaAlimenticiciaIAService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)  // Importante: asigna el cliente con el interceptor
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RutinaAlimenticiciaIAService::class.java)
    }
}