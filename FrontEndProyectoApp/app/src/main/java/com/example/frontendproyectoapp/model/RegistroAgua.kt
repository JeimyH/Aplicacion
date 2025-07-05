package com.example.frontendproyectoapp.model

data class RegistroAgua(
    val idRegistro: Long? = null,
    val idUsuario: Long,
    val fecha: String,
    val cantidadml: Int
)
