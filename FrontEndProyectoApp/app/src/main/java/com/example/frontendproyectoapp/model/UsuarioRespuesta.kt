package com.example.frontendproyectoapp.model

data class UsuarioRespuesta(
    val id_usuario: Long,
    val correo: String,
    val nombre: String,
    val fechaNacimiento: String,
    val altura: Float,
    val peso: Float,
    val sexo: String?,
    val restriccionesDieta: String?,
    val objetivosSalud: String?
)