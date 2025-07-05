package com.example.frontendproyectoapp.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color

// Colores personalizados para DietaSmart
val PastelMint = Color(0xFFA8D5BA)       // Primario
val PastelPeach = Color(0xFFFFC8A2)      // Secundario
val PastelYellow = Color(0xFFFFF4B1)     // Acento
val PastelBackground = Color(0xFFFFFDF7) // Fondo
val SurfaceLight = Color(0xFFF3F3F3)     // Superficie
val TextOlive = Color(0xFF5E716A)        // Texto oscuro
val PastelError = Color(0xFFF8B5B1)      // Error o advertencias

// Colores para puntos de actividad en el calendario
val DotAgua = Color(0xFF9ED0F5)    // Azul pastel agua
val DotComida = Color(0xFFB4E3B0)  // Verde pastel comida
val DotAmbos = Color(0xFFD0B4E3)   // Morado pastel ambos

// Extensiones para usar en MaterialTheme.colorScheme
val ColorScheme.dotAgua: Color
    get() = DotAgua

val ColorScheme.dotComida: Color
    get() = DotComida

val ColorScheme.dotAmbos: Color
    get() = DotAmbos

