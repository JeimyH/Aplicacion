package com.example.frontendproyectoapp.model

data class RegistroAlimento (
    val idRegistroAlimento: Long? = null,
    val idAlimento: Long,
    val idUsuario: Long,
    val tamanoPorcion: Float,
    val unidadMedida: String,
    val favorito: Boolean,
    val momentoDelDia: String,
    val consumidoEn: String
)