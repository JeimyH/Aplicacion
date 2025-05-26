package com.example.frontendproyectoapp.model

data class RegistroAlimentoEntrada(
    val idUsuario: Long,
    val idAlimento: Long,
    val tamanoPorcion: Float,
    val unidadMedida: String,
    val momentoDelDia: String
)
