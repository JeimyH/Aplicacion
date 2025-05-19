package com.example.frontendproyectoapp.model

data class PreferenciasUsuario (
    val id_preferencia: Long,
    val objetivoAguaDiario: Float,
    val comidasPreferidas: String,
    val alimentosExluidos:String,
    val configuracionesNotificaciones: String
)