package com.example.frontendproyectoapp.model

import java.sql.Time

data class Recordatorio (
    val id_recordatorio: Long,
    val tipoRecordatorio: TipoRecordatorio,
    val mensaje: String,
    val hora: Time,
    val activo: Boolean
)
enum class TipoRecordatorio {
    Agua,
    Comida,
    Actividad,
    Personalizado
}