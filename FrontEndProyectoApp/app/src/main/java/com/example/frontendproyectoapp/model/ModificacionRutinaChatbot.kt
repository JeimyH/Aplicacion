package com.example.frontendproyectoapp.model

import java.sql.Timestamp

data class ModificacionRutinaChatbot (
    val id_modificacion: Long,
    val fecha: Timestamp,
    val accion: Accion,
    val comida: String,
    val motivo: String
)
enum class Accion {
    Agregar,
    Eliminar,
    Cambiar
}