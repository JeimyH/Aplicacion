package com.example.frontendproyectoapp.viewModel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontendproyectoapp.model.Alimento
import com.example.frontendproyectoapp.model.RegistroAlimentoSalida
import com.example.frontendproyectoapp.model.UserPreferences
import com.example.frontendproyectoapp.repository.BuscarAlimentoRepository
import com.example.frontendproyectoapp.repository.FavoritosRepository
import com.example.frontendproyectoapp.repository.RegistroAlimentoRepository
import kotlinx.coroutines.launch

class BuscarAlimentoViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext
    private val repository = BuscarAlimentoRepository()
    private val registroRepository = RegistroAlimentoRepository()

    var listaAlimentos by mutableStateOf<List<Alimento>>(emptyList())
    var alimentosFiltrados by mutableStateOf<List<Alimento>>(emptyList())
    var comidasRecientes by mutableStateOf<List<RegistroAlimentoSalida>>(emptyList())
    var favoritos by mutableStateOf<List<Alimento>>(emptyList())
    var alimentosRecientes by mutableStateOf<List<Alimento>>(emptyList())
        private set
    var busqueda by mutableStateOf("")
    var idUsuario: Long? = null

    var alimentosAgrupados by mutableStateOf<Map<String, List<Alimento>>>(emptyMap())

    init {
        viewModelScope.launch {
            idUsuario = UserPreferences.obtenerIdUsuarioActual(context)
            cargarDatos()
        }
    }

    private val favoritosRepository = FavoritosRepository()

    init {
        viewModelScope.launch {
            idUsuario = UserPreferences.obtenerIdUsuarioActual(context)
            idUsuario?.let {
                cargarDatos()
                cargarFavoritos()
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
        alimentosRecientes = buildList {
            add(alimento)
            addAll(alimentosRecientes.filter { it.idAlimento != alimento.idAlimento })
            if (size > 5) removeAt(lastIndex)

        }
    }

    fun cargarComidasRecientes() {
        viewModelScope.launch {
            try {
                val idUsuario = UserPreferences.obtenerIdUsuarioActual(context)
                if (idUsuario != null) {
                    comidasRecientes = registroRepository.obtenerComidasRecientes(idUsuario)
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
                    "Fruta", "Vegetal", "Pescado","Carne", "Proteína vegetal",
                    "Grasa", "Cereal","Lácteo", "Dulce", "Fruto seco", "Semilla"
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

                // Favoritos actuales del usuario (si ya existe)
                idUsuario?.let {
                    favoritos = repository.obtenerFavoritos(it)
                }

            } catch (e: Exception) {
                Log.e("BuscarVM", "Error general al cargar alimentos: ${e.message}")
            }
        }
    }
}
