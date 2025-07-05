package com.example.frontendproyectoapp.model

data class ActividadDia(
    val fecha: String, // formato esperado: "yyyy-MM-dd"
    val tipo: String    // "AGUA", "COMIDA" o "AMBOS"
)