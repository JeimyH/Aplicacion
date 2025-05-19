package com.example.frontendproyectoapp.interfaces

import com.example.frontendproyectoapp.model.Login
import com.example.frontendproyectoapp.model.Usuario
import com.example.frontendproyectoapp.model.UsuarioEntrada
import com.example.frontendproyectoapp.model.UsuarioRespuesta
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

interface UsuarioService {
    @GET("/api/Usuario/listar")
    suspend fun listarUsuario(): List<Usuario>

    @GET("/api/Usuario/buscar/{id_usuario}")
    suspend fun listarPorIdUsuario(@Path("id_usuario") id_usuario: Long): Usuario

    @POST("/api/Usuario/guardar")
    suspend fun guardarUsuario(@Body usuario: Usuario): Usuario

    @DELETE("/api/Usuario/eliminar/{id_usuario}")
    suspend fun eliminarUsuario(@Path("id_usuario") id_usuario: Long)

    @PUT("/api/Usuario/actualizar/{id_usuario}")
    suspend fun actualizarUsuario(@Path("id_usuario") id_usuario: Long, @Body usuario: Usuario): Usuario

    @POST("/api/Usuario/registrar")
    suspend fun registrarUsuario(@Body usuarioEntrada: UsuarioEntrada): UsuarioRespuesta

    @POST("/api/Usuario/login")
    suspend fun login(@Body login: Login): UsuarioRespuesta

}

object RetrofitClientUsuario {
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

    val usuarioService: UsuarioService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)  // Importante: asigna el cliente con el interceptor
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UsuarioService::class.java)
    }
}