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
import retrofit2.http.Query

interface AlimentoService {
    @GET("/api/Alimento/listar")
    suspend fun listarAlimentos(): List<Alimento>

    @GET("/api/Alimento/buscar/{idAlimento}")
    suspend fun listarIdAlimento(@Path("idAlimento") idAlimento: Long): Alimento

    @POST("/api/Alimento/guardar")
    suspend fun guardarAlimento(@Body alimento: Alimento): Alimento

    @DELETE("/api/Alimento/eliminar/{id_alimento}")
    suspend fun eliminarAlimento(@Path("id_alimento") id_alimento: Long)

    @PUT("/api/Alimento/actualizar/{id_alimento}")
    suspend fun actualizarAlimento(@Path("id_alimento") id_alimento: Long, @Body alimentoActualizado: Alimento): Alimento

    @GET("api/Alimento/alimentoCategoria/{categoria}")
    suspend fun obtenerAlimentosPorCategoria(@Path("categoria") categoria: String): List<Alimento>

    @GET("/api/Alimento/buscar/nombre/{nombre}")
    suspend fun obtenerAlimentoPorNombre(@Path("nombre") nombre: String): Alimento

    @GET("/api/Alimento/buscar/coincidencias/{nombre}")
    suspend fun buscarCoincidencias(@Path("nombre") nombre: String): List<Alimento>

    @GET("/api/Alimento/categoria/{categoria}")
    suspend fun listarPorCategoria(@Path("categoria") categoria: String): List<Alimento>

    @GET("/api/Alimento/favoritos/{idUsuario}")
    suspend fun obtenerFavoritos(@Path("idUsuario") idUsuario: Long): List<Alimento>

    @POST("/api/Alimento/favoritoAgregar/{idUsuario}/{idAlimento}")
    suspend fun marcarFavorito(
        @Path("idUsuario") idUsuario: Long,
        @Path("idAlimento") idAlimento: Long
    )

    @DELETE("/api/Alimento/favoritoEliminar/{idUsuario}/{idAlimento}")
    suspend fun eliminarFavorito(
        @Path("idUsuario") idUsuario: Long,
        @Path("idAlimento") idAlimento: Long
    )

    @GET("/api/Alimento/imagen")
    suspend fun obtenerUrlImagenPorNombre(@Query("nombre") nombre: String): String

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