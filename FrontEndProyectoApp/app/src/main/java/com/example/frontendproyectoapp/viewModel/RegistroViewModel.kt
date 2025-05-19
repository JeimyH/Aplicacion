package com.example.frontendproyectoapp.viewModel

import androidx.lifecycle.ViewModel

class RegistroViewModel : ViewModel(){
    var correo: String = ""
    var contrasena: String = ""
    var nombre: String = ""
    var fechaNacimiento: String = "" // <--- formato "yyyy-MM-dd"
    var altura: Float = 0f
    var peso: Float = 0f
    var sexo: String = ""
    var restriccionesDieta: String? = null
    var objetivosSalud: String = ""
    var pesoObjetivo: Float = 0f
}



