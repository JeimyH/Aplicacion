package com.example.frontendproyectoapp.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color

// Colores personalizados para DietaSmart
val PastelMint = Color(0xFFA8D5BA)       // Primario: Verde menta suave
val PastelLeaf = Color(0xFFC7E9C0)       // Verde hoja pálido
val PastelYellow = Color(0xFFF1F8A0)     // Acento amarillo pastel
val PastelBackground = Color(0xFFF2FBF2) // Fondo claro verdoso
val PastelWhite = Color(0xFFFFFFFF)      // Blanco neutro
val TextOlive = Color(0xFF5E716A)        // Texto principal verde oliva
val TextGray = Color(0xFF8A8A8A)         // Texto gris medio
val PastelError = Color(0xFFF8B5B1)      // Color de error o advertencia (rosado pastel)

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

