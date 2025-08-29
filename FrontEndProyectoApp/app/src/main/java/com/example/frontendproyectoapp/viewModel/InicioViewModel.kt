package com.example.frontendproyectoapp.viewModel

import android.app.Application
import android.content.Context
import android.util.Log

import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontendproyectoapp.model.ActividadDia
import com.example.frontendproyectoapp.model.NutrientesRecomendados
import com.example.frontendproyectoapp.model.NutrientesTotales
import com.example.frontendproyectoapp.DataStores.UserPreferences
import com.example.frontendproyectoapp.repository.InicioRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class InicioViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext
    private val repository = InicioRepository()

    var nutrientesTotales by mutableStateOf<NutrientesTotales?>(null)
        private set

    private val _nutrientes = mutableStateOf<NutrientesRecomendados?>(null)
    val nutrientes: State<NutrientesRecomendados?> = _nutrientes

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error


    var errorT by mutableStateOf<String?>(null)
        private set

    var vasosConsumidosHoy by mutableStateOf(0)
        private set

    var estadoCarga by mutableStateOf(false)
        private set

    private val _mensajeUI = MutableSharedFlow<String>()
    val mensajeUI: SharedFlow<String> = _mensajeUI

    private val _diasConActividad = mutableStateListOf<ActividadDia>()
    val diasConActividad: List<ActividadDia> get() = _diasConActividad

    private var idUsuarioActual: Long? = null

    private val _idUsuario = MutableStateFlow<Long?>(null)
    val idUsuario: StateFlow<Long?> = _idUsuario.asStateFlow()

    fun cargarIdUsuario(context: Context) {
        viewModelScope.launch {
            UserPreferences.obtenerIdUsuario(context).collect {
                _idUsuario.value = it
            }
        }
    }

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
                    Log.d("InicioViewModel", "Registro cargado de agua: ${registro?.cantidadml} ml")
                }.onFailure {
                    vasosConsumidosHoy = 0
                    Log.e("InicioViewModel", "Error al obtener registro de hoy: ${it.message}")
                }
            } catch (e: Exception) {
                vasosConsumidosHoy = 0
                Log.e("InicioViewModel", "Excepción al cargar registro de hoy: ${e.message}")
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

    fun cargarTotalesDelDia(context: Context) {
        viewModelScope.launch {
            try {
                val idUsuarioActual = UserPreferences.obtenerIdUsuarioActual(context)
                val hoy = LocalDate.now(ZoneId.systemDefault())
                val fechaStr = hoy.format(DateTimeFormatter.ISO_DATE)

                Log.d("InicioVM", "Cargando totales nutricionales...")
                Log.d("InicioVM", "→ ID Usuario: $idUsuarioActual")
                Log.d("InicioVM", "→ Fecha solicitada: $fechaStr")

                if (idUsuarioActual != null) {
                    val resultado = repository.obtenerTotalesPorFecha(
                        idUsuario = idUsuarioActual,
                        fecha = LocalDate.parse(fechaStr)
                    )
                    Log.d("InicioVM", "✔ Totales recibidos: $resultado")

                    nutrientesTotales = resultado
                    errorT = null
                } else {
                    errorT = "Usuario no autenticado"
                    Log.e("EstadisticasVM", "✖ No se encontró el ID del usuario.")
                }

            } catch (e: Exception) {
                errorT = "Error al obtener totales: ${e.localizedMessage}"
                Log.e("EstadisticasVM", "✖ Excepción: ${e.message}", e)
            }
        }
    }


    fun cargarRecomendaciones(idUsuario: Long) {
        viewModelScope.launch {
            val resultado = repository.obtenerRecomendaciones(idUsuario)
            resultado.onSuccess {
                _nutrientes.value = it
            }.onFailure {
                _error.value = it.message
            }
        }
    }
}

