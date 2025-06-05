package com.example.frontendproyectoapp.model

import java.sql.Timestamp

data class ConfiguracionAplicacion (
    val id_configuracion: Long,
    val idioma: Idioma,
    val notificaciones: Boolean,
    val tema: Tema,
    val frecuenciaActualizacion: FrecuenciaActualizacion,
    val creadoEn: Timestamp
)
enum class Idioma {
    Espanol,
    Ingles
}

enum class Tema {
    Claro,
    Oscuro
}

enum class FrecuenciaActualizacion {
    Diaria,
    Semanal,
    Mensual
}