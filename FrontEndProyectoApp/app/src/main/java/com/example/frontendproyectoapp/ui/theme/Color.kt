package com.example.frontendproyectoapp.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color

// Colores base
val PastelMint = Color(0xFF91CF90)       // Verde menta más natural // Botones principales
val PastelLeaf = Color(0xFFB4D9A6)       // Verde hoja
val PastelYellow = Color(0xFFFFEDA1)     // Amarillo banana suave
val PastelBackground = Color(0xFFF8FFF0) // Fondo claro con tinte amarillo verdoso
val PastelWhite = Color(0xFFFFFCF2)      // Blanco cálido tipo crema
val TextOlive = Color(0xFF5E716A)        // Verde oliva para texto
val TextGray = Color(0xFF8A8A8A)         // Gris medio neutro
val PastelError = Color(0xFFCE90CF)      // Rosado pastel (sin cambios)
val Pastel = Color(0xFFF2F5FF)           // trackColor-LinearProgress

// Colores de puntos para actividades del calendario
val DotAgua = Color(0xFF9CCFF5)     // Azul pastel agua (ligeramente más brillante)
val DotComida = Color(0xFFA9D69A)   // Verde pastel más cercano a las manzanas/aguacate
val DotAmbos = Color(0xFFD6B5E3)    // Morado pastel más claro

// Extensiones para usar en MaterialTheme.colorScheme
val ColorScheme.dotAgua: Color
    get() = DotAgua

val ColorScheme.dotComida: Color
    get() = DotComida

val ColorScheme.dotAmbos: Color
    get() = DotAmbos

