package com.example.frontendproyectoapp.model

import java.sql.Timestamp

data class RegistroAgua(
    val id_registroAgua: Long,
    val cantidadml: Float,
    val registradoEn: Timestamp
)