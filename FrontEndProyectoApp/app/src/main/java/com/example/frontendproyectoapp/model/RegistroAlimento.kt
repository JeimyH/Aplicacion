package com.example.frontendproyectoapp.model

data class RegistroAlimento (
    val id_registroAlimento: Long,
    val tamanoPorcion: Float,
    val unidadMedida: String,
    val favorito: Boolean,
    val momentoDelDia: String,
    val consumidoEn: String? = null
)