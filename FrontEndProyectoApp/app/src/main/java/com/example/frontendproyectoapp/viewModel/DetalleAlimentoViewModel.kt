package com.example.frontendproyectoapp.viewModel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontendproyectoapp.model.RegistroAlimentoEntrada
import com.example.frontendproyectoapp.DataStores.UserPreferences
import com.example.frontendproyectoapp.repository.AlimentoRepository
import kotlinx.coroutines.launch

class DetalleAlimentoViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext
    private val repository = AlimentoRepository()

    val estadoRegistro = mutableStateOf(false)
    val error = mutableStateOf<String?>(null)

    fun registrarAlimento(
        idAlimento: Long,
        cantidad: Float,
        unidad: String,
        momento: String
    ) {
        viewModelScope.launch {
            try {
                val idUsuario = UserPreferences.obtenerIdUsuarioActual(context)
                if (idUsuario != null) {
                    val registro = RegistroAlimentoEntrada(
                        idUsuario = idUsuario,
                        idAlimento = idAlimento,
                        tamanoPorcion = 0f,
                        unidadMedida = "",
                        tamanoOriginal = cantidad,
                        unidadOriginal = unidad,
                        momentoDelDia = momento
                    )
                    repository.guardarRegistro(registro)
                    estadoRegistro.value = true
                    error.value = null
                } else {
                    error.value = "Usuario no autenticado"
                }
            } catch (e: Exception) {
                error.value = e.message
                estadoRegistro.value = false
            }
        }
    }
}
