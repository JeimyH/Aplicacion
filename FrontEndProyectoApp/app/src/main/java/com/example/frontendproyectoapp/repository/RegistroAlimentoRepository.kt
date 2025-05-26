package com.example.frontendproyectoapp.repository

import com.example.frontendproyectoapp.interfaces.RetrofitClientRegistroAlimento
import com.example.frontendproyectoapp.model.RegistroAlimentoEntrada
import com.example.frontendproyectoapp.model.RegistroAlimentoSalida

class RegistroAlimentoRepository {
        suspend fun guardarRegistro(registro: RegistroAlimentoEntrada) {
        RetrofitClientRegistroAlimento.registroAlimentoService.guardarRegistro(registro)
    }

    suspend fun obtenerComidasRecientes(idUsuario: Long): List<RegistroAlimentoSalida> {
        return RetrofitClientRegistroAlimento.registroAlimentoService.obtenerComidasRecientes(idUsuario)
    }
}
