package com.example.frontendproyectoapp.viewModel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontendproyectoapp.model.Alimento
import com.example.frontendproyectoapp.model.RegistroAlimentoEntrada
import com.example.frontendproyectoapp.model.RegistroAlimentoSalida
import com.example.frontendproyectoapp.DataStores.UserPreferences
import com.example.frontendproyectoapp.repository.AlimentoRepository
import com.example.frontendproyectoapp.repository.FavoritosRepository
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

class AlimentoViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext
    private val repository = AlimentoRepository()
    private val favoritosRepository = FavoritosRepository()

    var errorCarga by mutableStateOf<String?>(null)
    var listaAlimentos by mutableStateOf<List<Alimento>>(emptyList())
    var alimentosFiltrados by mutableStateOf<List<Alimento>>(emptyList())
    var comidasRecientes by mutableStateOf<List<RegistroAlimentoSalida>>(emptyList())
    var favoritos by mutableStateOf<List<Alimento>>(emptyList())
    var alimentosRecientes by mutableStateOf<List<Alimento>>(emptyList()) // ← MISMO TIPO
        private set
    var busqueda by mutableStateOf("")
    var idUsuario: Long? = null
    var alimentosAgrupados by mutableStateOf<Map<String, List<Alimento>>>(emptyMap())

    private val _unidades = mutableStateOf<List<String>>(emptyList())
    val unidades: State<List<String>> = _unidades

    private val _error = mutableStateOf<String?>(null)
    val error: State<String?> = _error

    var mostrarDialogoRegistro = mutableStateOf(false)
    var momentoSeleccionado by mutableStateOf("")

    private val flexibleIsoFormatter: DateTimeFormatter = DateTimeFormatterBuilder()
        .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
        .optionalStart()
        .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
        .optionalEnd()
        .toFormatter()

    init {
        viewModelScope.launch {
            idUsuario = UserPreferences.obtenerIdUsuarioActual(context)
            idUsuario?.let {
                cargarDatos()
                cargarFavoritos()
                cargarComidasRecientes()
                cargarAlimentosRecientes() // ← NUEVO
            }
        }
    }

    fun actualizarUsuarioYDatos() {
        viewModelScope.launch {
            idUsuario = UserPreferences.obtenerIdUsuarioActual(context)
            idUsuario?.let {
                cargarDatos()
                cargarFavoritos()
                cargarComidasRecientes()
                cargarAlimentosRecientes()
            }
        }
    }

    fun cargarFavoritos() {
        viewModelScope.launch {
            idUsuario?.let {
                favoritos = favoritosRepository.obtenerFavoritos(it)
            }
        }
    }

    fun toggleFavorito(alimento: Alimento) {
        viewModelScope.launch {
            try {
                idUsuario?.let { uid ->
                    if (esFavorito(alimento.idAlimento)) {
                        favoritosRepository.eliminarFavorito(uid, alimento.idAlimento)
                    } else {
                        favoritosRepository.marcarFavorito(uid, alimento.idAlimento)
                    }
                    cargarFavoritos()
                }
            } catch (e: Exception) {
                Log.e("BuscarVM", "Error actualizando favorito: ${e.message}")
            }
        }
    }

    fun cargarDatos() {
        viewModelScope.launch {
            try {
                idUsuario?.let {
                    listaAlimentos = repository.obtenerTodos()
                    favoritos = repository.obtenerFavoritos(it)
                    aplicarFiltro()
                }
            } catch (e: Exception) {
                errorCarga = "No se pudo cargar la lista de alimentos. Verifica tu conexión."
                Log.e("BuscarVM", "Error al cargar datos: ${e.message}")
            }
        }
    }

    fun buscarEnTiempoReal(nombre: String) {
        busqueda = nombre
        aplicarFiltro()
    }

    private fun aplicarFiltro() {
        alimentosFiltrados = if (busqueda.isBlank()) {
            listaAlimentos
        } else {
            listaAlimentos.filter {
                it.nombreAlimento.contains(busqueda, ignoreCase = true)
            }
        }
    }

    fun esFavorito(idAlimento: Long): Boolean {
        return favoritos.any { it.idAlimento == idAlimento }
    }

    fun agregarARecientes(alimento: Alimento) {
        viewModelScope.launch {
            try {
                idUsuario?.let {
                    repository.registrarAlimentoReciente(it, alimento.idAlimento)
                    cargarAlimentosRecientes() // refresca
                }
            } catch (e: Exception) {
                Log.e("BuscarVM", "Error registrando reciente: ${e.message}")
            }
        }
    }

    fun cargarAlimentosRecientes() {
        viewModelScope.launch {
            try {
                idUsuario?.let {
                    val recientes = repository.obtenerAlimentosRecientes(it)
                    alimentosRecientes = recientes.take(5).map { it.alimento } // solo los alimentos
                }
            } catch (e: Exception) {
                Log.e("BuscarVM", "Error cargando alimentos recientes: ${e.message}")
            }
        }
    }

    fun cargarComidasRecientes() {
        viewModelScope.launch {
            try {
                val idUsuario = UserPreferences.obtenerIdUsuarioActual(context)
                if (idUsuario != null) {
                    val todosLosRegistros = repository.obtenerComidasRecientes(idUsuario)

                    val zonaLocal = ZoneId.systemDefault()
                    val hoyLocal = LocalDate.now(zonaLocal)
                    val inicioHoyLocal = hoyLocal.atStartOfDay(zonaLocal)
                    val finHoyLocal = inicioHoyLocal.plusDays(1).minusNanos(1)
                    Log.d("BuscarVM", "Fecha local actual: $hoyLocal")
                    Log.d("BuscarVM", "Inicio día local: $inicioHoyLocal")
                    Log.d("BuscarVM", "Fin día local: $finHoyLocal")

                    val formatterBD = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")

                    comidasRecientes = todosLosRegistros.filter { registro ->
                        try {
                            //val fechaLocalDateTime = LocalDateTime.parse(registro.consumidoEn, formatterBD)
                            val fechaLocalDateTime = LocalDateTime.parse(registro.consumidoEn, flexibleIsoFormatter)
                            val fechaLocalZoned = fechaLocalDateTime.atZone(zonaLocal) // <-- Aquí el cambio
                            Log.d("BuscarVM", "Registro: ${registro.alimento?.nombreAlimento} | Local Zoned: $fechaLocalZoned")
                            fechaLocalZoned >= inicioHoyLocal && fechaLocalZoned <= finHoyLocal
                        } catch (e: Exception) {
                            Log.e("BuscarVM", "Error parseando fecha: ${registro.consumidoEn} - ${e.message}")
                            false
                        }
                    }

                    Log.d("BuscarVM", "Comidas filtradas hoy: ${comidasRecientes.size}")
                }
            } catch (e: Exception) {
                Log.e("BuscarVM", "Error al obtener comidas recientes: ${e.message}")
            }
        }
    }

    fun cargarAlimentosAgrupados() {
        viewModelScope.launch {
            try {
                idUsuario = UserPreferences.obtenerIdUsuarioActual(context)

                val categorias = listOf(
                    "Fruta", "Vegetal", "Pescado", "Carne", "Proteína vegetal",
                    "Grasa", "Cereal", "Lácteo", "Dulce", "Fruto seco", "Semilla"
                )

                val agrupados = mutableMapOf<String, List<Alimento>>()

                for (categoria in categorias) {
                    try {
                        val alimentos = repository.obtenerAlimentosPorCategoria(categoria)
                        if (alimentos.isNotEmpty()) {
                            agrupados[categoria] = alimentos
                        }
                    } catch (e: Exception) {
                        Log.e("BuscarVM", "Error cargando categoría $categoria: ${e.message}")
                    }
                }

                alimentosAgrupados = agrupados

                idUsuario?.let {
                    favoritos = repository.obtenerFavoritos(it)
                }
            } catch (e: Exception) {
                Log.e("BuscarVM", "Error general al cargar alimentos: ${e.message}")
            }
        }
    }

    fun eliminarTodosRecientes() {
        viewModelScope.launch {
            try {
                idUsuario?.let {
                    Log.d("BuscarVM", "Eliminando TODOS los alimentos recientes del usuario $it")
                    repository.eliminarTodosRecientes(it)
                    alimentosRecientes = emptyList()
                }
            } catch (e: Exception) {
                Log.e("BuscarVM", "Error al eliminar recientes: ${e.message}")
            }
        }
    }

    fun eliminarRecienteIndividual(idAlimento: Long) {
        viewModelScope.launch {
            try {
                idUsuario?.let {
                    Log.d("BuscarVM", "Eliminando alimento reciente con ID $idAlimento para usuario $it")
                    repository.eliminarRecienteIndividual(it, idAlimento)
                    alimentosRecientes = alimentosRecientes.filter { it.idAlimento != idAlimento }
                }
            } catch (e: Exception) {
                Log.e("BuscarVM", "Error al eliminar reciente individual: ${e.message}")
            }
        }
    }

    fun registrarAlimentoDesdeDialogo(
        idAlimento: Long,
        cantidad: Float,
        unidad: String,
        momento: String
    ) {
        viewModelScope.launch {
            try {
                idUsuario?.let { uid ->
                    val dto = RegistroAlimentoEntrada(
                        idUsuario = uid,
                        idAlimento = idAlimento,
                        tamanoPorcion = 0f,
                        unidadMedida = "",
                        tamanoOriginal = cantidad,
                        unidadOriginal = unidad,
                        momentoDelDia = momento
                    )
                    Log.d("AlimentoVM", "Preparando DTO para enviar: $dto")
                    repository.guardarRegistro(dto)
                    Log.d("AlimentoVM", "✔ Registro enviado exitosamente")
                    cargarComidasRecientes()
                }
            } catch (e: Exception) {
                Log.e("AlimentoVM", "✖ Error registrando alimento desde diálogo: ${e.message}")
            }
        }
    }

    fun eliminarRegistrosPorMomentoYFecha(momento: String) {
        viewModelScope.launch {
            try {
                val idUsuarioActual = UserPreferences.obtenerIdUsuarioActual(context)
                val hoy = LocalDate.now(ZoneId.systemDefault())
                val fechaStr = hoy.format(DateTimeFormatter.ISO_DATE)

                Log.d("BuscarVM", "Intentando eliminar registros para:")
                Log.d("BuscarVM", "→ ID Usuario: $idUsuarioActual")
                Log.d("BuscarVM", "→ Fecha: $fechaStr")
                Log.d("BuscarVM", "→ Momento del día: $momento")

                if (idUsuarioActual != null) {
                    val response = repository.eliminarRegistrosPorFechaYMomento(
                        idUsuario = idUsuarioActual,
                        fecha = fechaStr,
                        momento = momento
                    )
                    if (response.isSuccessful) {
                        Log.d("BuscarVM", "✔ Registros eliminados correctamente para $momento")
                        cargarComidasRecientes()
                    } else {
                        Log.e("BuscarVM", "✖ Error al eliminar registros: ${response.code()} ${response.message()}")
                    }
                }
            } catch (e: Exception) {
                Log.e("BuscarVM", "✖ Excepción al eliminar registros: ${e.message}")
            }
        }
    }

    fun eliminarRegistroIndividual(idRegistro: Long) {
        viewModelScope.launch {
            try {
                repository.eliminarRegistroPorId(idRegistro)
                cargarComidasRecientes()
            } catch (e: Exception) {
                Log.e("BuscarVM", "Error al eliminar registro individual: ${e.message}")
            }
        }
    }

    fun cargarUnidadesPorId(idAlimento: Long) {
        viewModelScope.launch {
            try {
                _unidades.value = repository.obtenerUnidadesPorId(idAlimento)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun cargarUnidadesPorNombre(nombreAlimento: String) {
        viewModelScope.launch {
            try {
                _unidades.value = repository.obtenerUnidadesPorNombre(nombreAlimento)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}

