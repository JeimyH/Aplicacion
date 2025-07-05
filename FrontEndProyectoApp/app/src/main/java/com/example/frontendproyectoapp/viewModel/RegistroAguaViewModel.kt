package com.example.frontendproyectoapp.viewModel

import android.app.Application
import android.util.Log

import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontendproyectoapp.model.ActividadDia
import com.example.frontendproyectoapp.model.UserPreferences
import com.example.frontendproyectoapp.repository.RegistroAguaRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class RegistroAguaViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext
    private val repository = RegistroAguaRepository()

    var vasosConsumidosHoy by mutableStateOf(0)
        private set

    var estadoCarga by mutableStateOf(false)
        private set

    private val _mensajeUI = MutableSharedFlow<String>()
    val mensajeUI: SharedFlow<String> = _mensajeUI

    private val _diasConActividad = mutableStateListOf<ActividadDia>()
    val diasConActividad: List<ActividadDia> get() = _diasConActividad

    var cargando by mutableStateOf(false)
        private set

    private var idUsuarioActual: Long? = null

    fun establecerIdUsuario(id: Long?) {
        idUsuarioActual = id
    }

    fun cargarDiasConActividad() {
        viewModelScope.launch {
            val idUsuario = idUsuarioActual ?: return@launch
            val resultado = repository.obtenerDiasConActividad(idUsuario)
            if (resultado.isSuccess) {
                _diasConActividad.clear()
                _diasConActividad.addAll(resultado.getOrDefault(emptyList()))
            }
        }
    }

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
            val idUsuario = UserPreferences.obtenerIdUsuarioActual(context)
            if (idUsuario != null) {
                if (vasosConsumidosHoy == 0) {
                    repository.eliminarRegistroDeHoy(idUsuario).onSuccess {
                        _mensajeUI.emit("Registro de agua eliminado")
                        cargarDatosUsuarioActual(idUsuario)
                        cargarDiasConActividad()
                    }.onFailure {
                        _mensajeUI.emit("Error al eliminar: ${it.message}")
                    }
                } else {
                    val cantidadml = vasosConsumidosHoy * 250
                    repository.registrarAgua(idUsuario, cantidadml).onSuccess {
                        _mensajeUI.emit("Registro actualizado: $cantidadml ml")
                        cargarDatosUsuarioActual(idUsuario)
                        cargarDiasConActividad()
                    }.onFailure {
                        _mensajeUI.emit("Error al registrar: ${it.message}")
                    }
                }
            }
        }

    }

}

