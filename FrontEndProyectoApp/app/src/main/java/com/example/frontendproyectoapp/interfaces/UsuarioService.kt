package com.example.frontendproyectoapp.interfaces

import com.example.frontendproyectoapp.model.Login
import com.example.frontendproyectoapp.model.UsuarioEntrada
import com.example.frontendproyectoapp.model.UsuarioRespuesta
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UsuarioService {

    @POST("/api/Usuario/login")
    suspend fun login(@Body login: Login): Response<UsuarioRespuesta>

    @POST("api/Usuario/registrar")
    suspend fun registrarUsuario(@Body usuario: UsuarioEntrada): Response<UsuarioRespuesta>

    @GET("/api/Usuario/existeCorreo")
    suspend fun verificarCorreoExistente(@Query("correo") correo: String): Boolean

    @GET("/api/Usuario/existeNombre")
    suspend fun verificarNombreExistente(@Query("nombre") nombre: String): Boolean

}

object RetrofitClientUsuario {
    // private const val BASE_URL = "http://10.0.2.2:8080"
    private const val BASE_URL = "http://192.168.1.4:8080"
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