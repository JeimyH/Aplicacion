package com.example.frontendproyectoapp.model

data class Alimento (
    val id_alimento: Long,
    val nombreAlimento: String,
    val calorias: Float,
    val proteinas: Float,
    val carbohidratos: Float,
    val grasas: Float,
    val azucares: Float,
    val fibra: Float,
    val sodio: Float,
    val grasasSaturadas: Float,
    val categoria: String
)