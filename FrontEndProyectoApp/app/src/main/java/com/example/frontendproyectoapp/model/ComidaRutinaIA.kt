package com.example.frontendproyectoapp.model

data class ComidaRutinaIA (
    val id_comida: Long,
    val tipoComida: TipoComida,
    val alimentosSugeridos:String,
    val valoresNutricionales: String,
    val tamanoPorciones: String,
    val diaNumero: Int,
    val diaSemana: DiaSemana
)
enum class TipoComida {
    Desayuno,
    Almuerzo,
    Cena,
    Snack
}

enum class DiaSemana {
    Lunes,
    Martes,
    Miercoles,
    Jueves,
    Vierner,
    Sabado,
    Domingo
}