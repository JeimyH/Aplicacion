package com.example.frontendproyectoapp.viewModel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontendproyectoapp.model.EstadisticaPorDia
import com.example.frontendproyectoapp.model.EstadisticaPorMes
import com.example.frontendproyectoapp.model.NutrientesRecomendados
import com.example.frontendproyectoapp.model.NutrientesTotales
import com.example.frontendproyectoapp.DataStores.UserPreferences
import com.example.frontendproyectoapp.repository.EstadisticasRepository
import com.example.frontendproyectoapp.repository.InicioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Month
import java.time.Year
import java.time.format.TextStyle
import java.util.Locale

class EstadisticasViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext
    private val repository = EstadisticasRepository()
    private val inicioRepository = InicioRepository()

    private val _idUsuario = MutableStateFlow<Long?>(null)
    val idUsuario: StateFlow<Long?> = _idUsuario.asStateFlow()

    var tipoVista by mutableStateOf(TipoVista.DIA) // DIA o MES

    // Ahora nutrienteSeleccionado es un State
    private val _nutrienteSeleccionado = mutableStateOf(Nutriente.CALORIAS)
    val nutrienteSeleccionado: State<Nutriente> = _nutrienteSeleccionado

    var anioActual = Year.now().value
    var mesActual = LocalDate.now().monthValue

    var datosDiarios by mutableStateOf<List<EstadisticaPorDia>>(emptyList())
        private set
    var datosMensuales by mutableStateOf<List<EstadisticaPorMes>>(emptyList())
        private set

    var nutrientesRecomendados by mutableStateOf<NutrientesRecomendados?>(null)
        private set

    var nutrientesRecomendadosMensuales by mutableStateOf<NutrientesRecomendados?>(null)
        private set

    var cargando by mutableStateOf(false)
        private set
    var error by mutableStateOf<String?>(null)
        private set

    init {
        viewModelScope.launch {
            val id = UserPreferences.obtenerIdUsuarioActual(context)
            _idUsuario.value = id
            id?.let {
                cargarDatos(it)
            }
        }
    }

    fun cargarDatos(uid: Long? = _idUsuario.value) {
        if (uid == null) return
        viewModelScope.launch {
            cargando = true
            error = null
            try {
                if (tipoVista == TipoVista.DIA) {
                    datosDiarios = repository.getConsumoPorDia(uid, anioActual, mesActual)
                    cargarRecomendaciones(uid) // Recomendaciones diarias
                } else {
                    datosMensuales = repository.getConsumoPorMes(uid, anioActual)
                    cargarRecomendacionesMensuales(uid, anioActual, mesActual) // Recomendaciones mensuales
                }
            } catch (e: Exception) {
                error = "Error cargando datos: ${e.message}"
                Log.e("EstadisticasVM", error ?: "")
            }
            cargando = false
        }
    }

    fun cargarRecomendaciones(idUsuario: Long) {
        viewModelScope.launch {
            try {
                inicioRepository.obtenerRecomendaciones(idUsuario)
                    .onSuccess { recomendaciones ->
                        nutrientesRecomendados = recomendaciones
                    }
                    .onFailure { e ->
                        error = "Error al obtener recomendaciones: ${e.message}"
                    }
            } catch (e: Exception) {
                error = "Excepción al obtener recomendaciones: ${e.message}"
            }
        }
    }

    fun cargarRecomendacionesMensuales(idUsuario: Long, anio: Int, mes: Int) {
        viewModelScope.launch {
            try {
                nutrientesRecomendadosMensuales =
                    repository.getRecomendacionesMensuales(idUsuario, anio, mes)
            } catch (e: Exception) {
                error = "Error al obtener recomendaciones mensuales: ${e.message}"
            }
        }
    }

    fun obtenerDatosParaGrafica(): List<Pair<Int, Float>> {
        return if (tipoVista == TipoVista.DIA) {
            datosDiarios.map { dia ->
                dia.dia to nutrienteSeleccionado.value.extractor(dia.nutrientes)
            }.filter { it.second > 0f } // Eliminar días sin consumo
        } else {
            datosMensuales.map { mes ->
                mes.mes to nutrienteSeleccionado.value.extractor(mes.nutrientes)
            }.filter { it.second > 0f } // Eliminar meses sin consumo
        }
    }

    fun cambiarMes(delta: Int) {
        if (tipoVista != TipoVista.DIA) return
        mesActual += delta
        if (mesActual > 12) {
            mesActual = 1
            anioActual++
        } else if (mesActual < 1) {
            mesActual = 12
            anioActual--
        }
        cargarDatos()
    }

    fun cambiarAnio(delta: Int) {
        anioActual += delta
        cargarDatos()
    }

    fun cambiarTipoVista(nuevoTipo: TipoVista) {
        tipoVista = nuevoTipo
        cargarDatos()
    }

    // Metodo para cambiar nutriente seleccionado
    fun cambiarNutriente(nuevo: Nutriente) {
        _nutrienteSeleccionado.value = nuevo
    }

    fun obtenerTituloFecha(): String {
        val nombreMes = Month.of(mesActual).getDisplayName(TextStyle.FULL, Locale("es"))
        return "$nombreMes $anioActual"
    }

    enum class TipoVista { DIA, MES }

    enum class Nutriente(
        val label: String,
        val unidad: String,
        val extractor: (NutrientesTotales) -> Float,
        val extractorRecomendados: (NutrientesRecomendados) -> Float
    ) {
        CALORIAS("Calorías", "kcal", { it.calorias }, { it.calorias }),
        PROTEINAS("Proteínas", "g", { it.proteinas }, { it.proteinas }),
        GRASAS("Grasas", "g", { it.grasas }, { it.grasas }),
        GRASAS_SAT("Grasas Saturadas", "g", { it.grasasSaturadas }, { it.grasasSaturadas }),
        AZUCARES("Azúcares", "g", { it.azucares }, { it.azucares }),
        FIBRA("Fibra", "g", { it.fibra }, { it.fibra }),
        SODIO("Sodio", "mg", { it.sodio }, { it.sodio }),
        CARBOHIDRATOS("Carbohidratos", "g", { it.carbohidratos }, { it.carbohidratos })
    }
}
