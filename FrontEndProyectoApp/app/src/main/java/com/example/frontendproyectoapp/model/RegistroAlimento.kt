package com.example.frontendproyectoapp.model

data class RegistroAlimento (
    val id_registroAlimento: Long,
    val tamanoPorcion: Float,
    val consumidoEn: String? = null,
    val urlImagen: String
)