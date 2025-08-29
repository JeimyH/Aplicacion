package com.example.frontendproyectoapp.model

data class RegistroAlimentoEntrada(
    val idUsuario: Long,
    val idAlimento: Long,
    val tamanoPorcion: Float,
    val unidadMedida: String,
    val tamanoOriginal: Float,     // <- nuevo campo requerido por el backend
    val unidadOriginal: String,
    val momentoDelDia: String
)
