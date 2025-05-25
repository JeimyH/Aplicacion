package com.example.frontendproyectoapp.viewModel

import android.app.Application
import android.util.Log

import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontendproyectoapp.model.UserPreferences
import com.example.frontendproyectoapp.repository.RegistroAguaRepository
import kotlinx.coroutines.launch

class RegistroAguaViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext
    private val repository = RegistroAguaRepository()

    var vasosConsumidosHoy by mutableStateOf(0)
        private set

    var estadoCarga by mutableStateOf(false)

    init {
        viewModelScope.launch {
            val idUsuario = UserPreferences.obtenerIdUsuarioActual(context)
            if (idUsuario != null) {
                cargarRegistroDeHoy(idUsuario)
            }
        }
    }

    private suspend fun cargarRegistroDeHoy(idUsuario: Long) {
        estadoCarga = true
        repository.obtenerRegistroDeHoy(idUsuario).onSuccess { registro ->
            vasosConsumidosHoy = (registro?.cantidadml ?: 0) / 250
        }
        estadoCarga = false
    }

    fun seleccionarCantidadVasos(nuevosVasos: Int) {
        vasosConsumidosHoy = nuevosVasos
    }

    fun registrarAgua() {
        viewModelScope.launch {
            val idUsuario = UserPreferences.obtenerIdUsuarioActual(context)
            if (idUsuario != null) {
                val cantidadml = vasosConsumidosHoy * 250
                repository.registrarAgua(idUsuario, cantidadml).onSuccess {
                    Log.d("InicioViewModel", "Registro actualizado: $it")
                }.onFailure {
                    Log.e("InicioViewModel", "Error al registrar: ${it.message}")
                }
            }
        }
    }
}