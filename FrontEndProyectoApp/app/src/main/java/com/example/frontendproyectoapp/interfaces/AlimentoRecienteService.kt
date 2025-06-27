package com.example.frontendproyectoapp.interfaces

import com.example.frontendproyectoapp.model.AlimentoReciente
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AlimentoRecienteService {

    @POST("api/Reciente/registrar/{idUsuario}/{idAlimento}")
    suspend fun registrarReciente(
        @Path("idUsuario") idUsuario: Long,
        @Path("idAlimento") idAlimento: Long
    ): Response<Void>

    @GET("api/Reciente/consultar/{idUsuario}")
    suspend fun obtenerRecientes(
        @Path("idUsuario") idUsuario: Long
    ): List<AlimentoReciente>

    @DELETE("api/Reciente/eliminarTodos/{idUsuario}")
    suspend fun eliminarTodos(@Path("idUsuario") idUsuario: Long): Response<Unit>

    @DELETE("api/Reciente/eliminar/{idUsuario}/{idAlimento}")
    suspend fun eliminarRecienteIndividual(
        @Path("idUsuario") idUsuario: Long,
        @Path("idAlimento") idAlimento: Long
    ): Response<Unit>

}

object RetrofitClientAlimentoReciente {

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

    val alimentoRecienteService: AlimentoRecienteService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)  // Importante: asigna el cliente con el interceptor
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AlimentoRecienteService::class.java)
    }
}