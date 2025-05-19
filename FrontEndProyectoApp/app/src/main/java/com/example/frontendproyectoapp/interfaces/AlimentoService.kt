package com.example.frontendproyectoapp.interfaces


import com.example.frontendproyectoapp.model.Alimento
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

interface AlimentoService {
    @GET("/api/Alimento/listar")
    suspend fun listarAlimentos(): List<Alimento>

    @GET("/api/Alimento/buscar/{id_alimento}")
    suspend fun listarIdAlimento(@Path("id_alimento") id_alimento: Long): Alimento

    @POST("/api/Alimento/guardar")
    suspend fun guardarAlimento(@Body alimento: Alimento): Alimento

    @DELETE("/api/Alimento/eliminar/{id_alimento}")
    suspend fun eliminarAlimento(@Path("id_alimento") id_alimento: Long)

    @PUT("/api/Alimento/actualizar/{id_alimento}")
    suspend fun actualizarAlimento(@Path("id_alimento") id_alimento: Long, @Body alimentoActualizado: Alimento): Alimento

    @GET("api/Alimento/alimentoNombre/{nombre}")
    suspend fun obtenerAlimentoPorNombre(@Path("nombre") nombre: String): Alimento

    @GET("api/Alimento/alimentoCategoria/{categoria}")
    suspend fun obtenerAlimentosPorCategoria(@Path("categoria") categoria: String): List<Alimento>

    //Pendiente
    @GET("api/Alimento/alimentoUsuario/{id_usuario}")
    suspend fun obtenerAlimentoPorUsuario(@Path("id_usuario") id_usuario: Long): List<Alimento>

    //Pendiente
    @GET("api/Alimento/InfNutricional/{id_alimento}")
    suspend fun obtenerInfNutricional(@Path("id_alimento") id_alimento: Long): Alimento
}

object RetrofitClientAlimento {

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

    val alimentoService: AlimentoService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)  // Importante: asigna el cliente con el interceptor
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AlimentoService::class.java)
    }
}