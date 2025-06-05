package com.example.frontendproyectoapp.model

import java.sql.Timestamp

data class InteraccionChatbot (
    val id_interaccion: Long,
    val consultaUsuario: String,
    val respuestaIA: String,
    val tipoIntento: TipoIntento,
    val tipoAccion: TipoAccion,
    val tema: String,
    val timestamp: Timestamp
)
enum class TipoIntento {
    Modificar_Rutina,
    Pregunta_Nutricional,
    Otros
}

enum class TipoAccion {
    Modificar,
    Agregar,
    Eliminar
}