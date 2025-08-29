package com.example.frontendproyectoapp.model

data class UsuarioEntrada(
    val correo: String,
    val contrasena: String,
    val nombre: String,
    val fechaNacimiento: String, // "yyyy-MM-dd"
    val altura: Float,
    val peso: Float,
    val sexo: String,
    val restriccionesDieta: String?,
    val objetivosSalud: String,
    val nivelActividad: String,
    val pesoObjetivo: Float

)