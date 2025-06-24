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
        private set

    // Metodo público para recargar los datos cuando cambia el usuario logueado
    fun cargarDatosUsuarioActual(idUsuario: Long?) {
        if (idUsuario == null) {
            vasosConsumidosHoy = 0
            return
        }
        viewModelScope.launch {
            estadoCarga = true
            try {
                repository.obtenerRegistroDeHoy(idUsuario).onSuccess { registro ->
                    vasosConsumidosHoy = (registro?.cantidadml ?: 0) / 250
                    Log.d("RegistroAguaViewModel", "Registro cargado: ${registro?.cantidadml} ml")
                }.onFailure {
                    vasosConsumidosHoy = 0
                    Log.e("RegistroAguaViewModel", "Error al obtener registro de hoy: ${it.message}")
                }
            } catch (e: Exception) {
                vasosConsumidosHoy = 0
                Log.e("RegistroAguaViewModel", "Excepción al cargar registro de hoy: ${e.message}")
            }
            estadoCarga = false
        }
    }

    fun seleccionarCantidadVasos(nuevosVasos: Int) {
        vasosConsumidosHoy = nuevosVasos
    }

    fun registrarAgua() {
        viewModelScope.launch {
            try {
                val idUsuario = UserPreferences.obtenerIdUsuarioActual(context)
                if (idUsuario != null) {
                    val cantidadml = vasosConsumidosHoy * 250
                    repository.registrarAgua(idUsuario, cantidadml).onSuccess {
                        Log.d("RegistroAguaViewModel", "Registro actualizado correctamente: $it")
                    }.onFailure {
                        Log.e("RegistroAguaViewModel", "Error al registrar agua: ${it.message}")
                    }
                } else {
                    Log.e("RegistroAguaViewModel", "ID de usuario nulo al registrar agua")
                }
            } catch (e: Exception) {
                Log.e("RegistroAguaViewModel", "Excepción al registrar agua: ${e.message}")
            }
        }
    }
}

