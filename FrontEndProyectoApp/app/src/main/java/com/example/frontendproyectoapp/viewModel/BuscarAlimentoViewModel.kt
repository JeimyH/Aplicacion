package com.example.frontendproyectoapp.viewModel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontendproyectoapp.model.Alimento
import com.example.frontendproyectoapp.model.RegistroAlimentoEntrada
import com.example.frontendproyectoapp.model.RegistroAlimentoSalida
import com.example.frontendproyectoapp.model.UserPreferences
import com.example.frontendproyectoapp.repository.BuscarAlimentoRepository
import com.example.frontendproyectoapp.repository.FavoritosRepository
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

class BuscarAlimentoViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext
    private val repository = BuscarAlimentoRepository()
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
                    Log.d("BuscarVM", "Fecha local actual: $hoyLocal")

                    comidasRecientes = todosLosRegistros.filter { registro ->
                        try {
                            val fechaUtc = LocalDateTime.parse(registro.consumidoEn, flexibleIsoFormatter)
                            val fechaLocal = fechaUtc.atZone(ZoneOffset.UTC).withZoneSameInstant(zonaLocal).toLocalDate()
                            Log.d("BuscarVM", "Registro: ${registro.alimento?.nombreAlimento} | Local: $fechaLocal")
                            fechaLocal == hoyLocal
                        } catch (e: Exception) {
                            Log.e("BuscarVM", "Error parseando fecha: ${registro.consumidoEn}")
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
                        tamanoPorcion = cantidad,
                        unidadMedida = unidad,
                        momentoDelDia = momento
                    )
                    repository.guardarRegistro(dto)
                    Log.d("BuscarVM", "Registro exitoso: alimento=$idAlimento, cantidad=$cantidad $unidad, momento=$momento")
                    cargarComidasRecientes()
                }
            } catch (e: Exception) {
                Log.e("BuscarVM", "Error registrando alimento desde diálogo: ${e.message}")
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

}

