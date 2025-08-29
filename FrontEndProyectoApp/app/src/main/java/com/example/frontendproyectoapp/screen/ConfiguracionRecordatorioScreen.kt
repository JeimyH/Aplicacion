package com.example.frontendproyectoapp.screen

import android.app.Application
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.frontendproyectoapp.DataStores.UserPreferences
import com.example.frontendproyectoapp.model.ReminderState
import com.example.frontendproyectoapp.viewModel.UsuarioViewModel
import com.example.frontendproyectoapp.viewModel.UsuarioViewModelFactory
import kotlinx.coroutines.flow.map
import java.util.Calendar

@Composable
fun ConfiguracionRecordatorioScreen(
    navController: NavHostController
) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val usuarioViewModel: UsuarioViewModel = viewModel(
        factory = UsuarioViewModelFactory(application)
    )

    // Recupera idUsuario desde DataStore
    val idUsuario by UserPreferences.obtenerIdUsuario(context)
        .map { it ?: 0L }
        .collectAsState(initial = 0L)

    LaunchedEffect(idUsuario) {
        if (idUsuario != 0L) {
            usuarioViewModel.cargarUsuario(idUsuario)
        }
    }

    ConfiguracionRecordatorioScreenContent(
        navController = navController,
        viewModel = usuarioViewModel,
        onBackClick = { navController.popBackStack() },
        idUsuario = idUsuario
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfiguracionRecordatorioScreenContent(
    navController: NavHostController,
    viewModel: UsuarioViewModel,
    onBackClick: () -> Unit = {},
    idUsuario: Long,
) {
    val context = LocalContext.current

    val secciones = mapOf(
        "Comidas" to listOf("desayuno", "almuerzo", "cena", "snack mañana", "snack tarde"),
        "Agua" to listOf("agua")
    )

    // Evita cargar recordatorios si el usuario aún no está listo
    LaunchedEffect(idUsuario) {
        if (idUsuario != 0L) {
            secciones.values.flatten().forEach { tipo ->
                viewModel.loadReminder(context, idUsuario, tipo)
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Detalles Alimento",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Atrás"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ){
            secciones.forEach { (titulo, tipos) ->
                Text(titulo, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                tipos.forEach { tipo ->
                    val reminderState = viewModel.reminders[tipo] ?: ReminderState(false, "08:00 AM")

                    RecordatorioCard(
                        tipo = tipo,
                        activo = reminderState.activo,
                        timeValue = reminderState.hora,
                        // No guarda nada si el idUsuario todavía es 0
                        onToggleActivo = { nuevoActivo ->
                            if (idUsuario != 0L) {
                                viewModel.updateReminder(context, idUsuario, tipo, activo = nuevoActivo)
                            }
                        },
                        onTimeSelected = { nuevaHora ->
                            if (idUsuario != 0L) {
                                viewModel.updateReminder(context, idUsuario, tipo, hora = nuevaHora)
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun RecordatorioCard(
    tipo: String,
    activo: Boolean,
    timeValue: String,
    onToggleActivo: (Boolean) -> Unit,
    onTimeSelected: (String) -> Unit
) {
    val context = LocalContext.current

    // Asegura que siempre se use el idUsuario actualizado en callbacks
    val currentOnTimeSelected by rememberUpdatedState(onTimeSelected)

    val (initHour, initMinute) = timeValue.split("[: ]".toRegex())
        .take(2)
        .map { it.toInt() }

    val timePicker = remember {
        TimePickerDialog(context, { _, h, m ->
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, h)
                set(Calendar.MINUTE, m)
            }
            val amPm = if (calendar.get(Calendar.AM_PM) == Calendar.AM) "AM" else "PM"
            val hour12 = if (calendar.get(Calendar.HOUR) == 0) 12 else calendar.get(Calendar.HOUR)
            val formatted = String.format("%02d:%02d %s", hour12, m, amPm)

            // Siempre usa la versión más reciente del callback
            currentOnTimeSelected(formatted)
        }, initHour, initMinute, false)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(tipo.replaceFirstChar { it.uppercase() })
                Spacer(Modifier.weight(1f))
                Switch(checked = activo, onCheckedChange = { onToggleActivo(it) })
            }
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Hora: $timeValue")
                Spacer(Modifier.weight(1f))
                IconButton(onClick = { timePicker.show() }) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar hora")
                }
            }
        }
    }
}
