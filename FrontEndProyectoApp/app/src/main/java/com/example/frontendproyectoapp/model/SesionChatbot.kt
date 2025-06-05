package com.example.frontendproyectoapp.model

import java.sql.Timestamp

data class SesionChatbot (
    val id_sesion: Long,
    val inicioSesion: Timestamp,
    val finSesion: Timestamp,
    val mensajes: String,
    val retroalimentacion: String
)