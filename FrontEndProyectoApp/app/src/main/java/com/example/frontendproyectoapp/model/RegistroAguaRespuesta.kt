package com.example.frontendproyectoapp.model

data class RegistroAguaRespuesta (
    val idRegistro: Long,
    val idUsuario: Long,
    val fecha: String,
    val cantidadml: Int
)