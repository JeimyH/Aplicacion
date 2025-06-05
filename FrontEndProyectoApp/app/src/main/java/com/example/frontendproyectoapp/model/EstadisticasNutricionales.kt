package com.example.frontendproyectoapp.model
import java.time.LocalDate

data class EstadisticasNutricionales (
    val id_estadistica: Long,
    val fecha: LocalDate,
    val totalCalorias: Float,
    val totalProteinas: Float,
    val totalCarbohidratos: Float,
    val totalGrasas: Float,
    val totalAzucares: Float,
    val totalFibra: Float,
    val totalSodio: Float,
    val totalGrasasSaturadas: Float,
    val totalAgua: Float,
    val totalComidas: Float,
    val imc: Float,
    val caloriasDesayuno: Float,
    val caloriasAlmuerzo: Float,
    val caloriasCena: Float,
    val caloriasSnack: Float,
)