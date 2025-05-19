package com.example.frontendproyectoapp.model

data class RutinaAlimenticiaIA (
    val id_rutina:Long,
    val fechaInicio: String,
    val fechaFin: String,
    val objetivoCaloricoDia: Float,
    val dias: String,
    val detalles: String,
    val creadoEn: String? = null,
    val actualizadoEn: String? = null
)