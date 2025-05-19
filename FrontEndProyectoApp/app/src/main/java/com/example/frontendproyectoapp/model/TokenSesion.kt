package com.example.frontendproyectoapp.model

import java.sql.Timestamp

data class TokenSesion (
    val id_token: Long,
    val token: String,
    val expiracion: Timestamp,
    val revocado: Boolean
)