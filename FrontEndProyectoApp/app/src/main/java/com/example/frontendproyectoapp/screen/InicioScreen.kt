package com.example.frontendproyectoapp.screen

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.frontendproyectoapp.model.UserPreferences
import com.example.frontendproyectoapp.viewModel.RegistroAguaViewModel
import com.example.frontendproyectoapp.viewModel.RegistroAguaViewModelFactory
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun InicioScreen(navController: NavHostController) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val viewModel: RegistroAguaViewModel = viewModel(
        factory = RegistroAguaViewModelFactory(application)
    )

    InicioScreenContent(viewModel = viewModel, navController = navController)
}

@Composable
fun InicioScreenContent(
    viewModel: RegistroAguaViewModel,
    navController: NavHostController
) {
    var selectedDate by remember { mutableStateOf(LocalDate.now(ZoneId.systemDefault())) }
    var mostrarLeyenda by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val idUsuarioFlow = produceState<Long?>(initialValue = null) {
        UserPreferences.obtenerIdUsuario(context).collect { value = it }
    }

    val cantidadVasos = 8
    val vasosConsumidos = viewModel.vasosConsumidosHoy
    val estadoCarga = viewModel.estadoCarga
    val diasConActividad = viewModel.diasConActividad

    val colorSeleccionado = MaterialTheme.colorScheme.primary
    val colorNoSeleccionado = MaterialTheme.colorScheme.surface
    val iconTintSeleccionado = MaterialTheme.colorScheme.onPrimary
    val iconTintNoSeleccionado = MaterialTheme.colorScheme.onSurface

    val snackbarHostState = remember { SnackbarHostState() }

    val diasConActividadMap = diasConActividad
        .groupBy { LocalDate.parse(it.fecha) }
        .mapValues { it.value.map { it.tipo }.toSet() }

    // Cargar datos del usuario
    LaunchedEffect(idUsuarioFlow.value) {
        viewModel.establecerIdUsuario(idUsuarioFlow.value)
        viewModel.cargarDatosUsuarioActual(idUsuarioFlow.value)
        viewModel.cargarDiasConActividad()
    }

    // Mostrar snackbar de mensaje
    LaunchedEffect(Unit) {
        viewModel.mensajeUI.collect { mensaje ->
            snackbarHostState.showSnackbar(mensaje)
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Botón ajustes
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = { navController.navigate("ajustes") }) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Ir a ajustes",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Calendario
            CustomCalendar(
                selectedDate = selectedDate,
                onDateSelected = { selectedDate = it },
                diasConActividad = diasConActividadMap
            )

            // Leyenda de calendario
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = { mostrarLeyenda = !mostrarLeyenda }) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Mostrar leyenda",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            LeyendaActividadCalendario(
                expandido = mostrarLeyenda,
                onDismiss = { mostrarLeyenda = false }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Texto calorías
            Text(
                "Calorías Totales / Calorías Recomendadas",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                "Kcal",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground
            )

            CaloriasGraph(caloriasMin = 1800, caloriasMax = 2200)

            Spacer(modifier = Modifier.height(8.dp))

            // Nutrientes
            NutrientRow("Proteínas", 70, 90)
            NutrientRow("Carbohidratos", 220, 250)
            NutrientRow("Grasas", 50, 70)
            NutrientRow("Azúcares", 30, 50)
            NutrientRow("Fibra", 20, 30)
            NutrientRow("Sodio", 1200, 2000)
            NutrientRow("Grasas Saturadas", 10, 20)

            Spacer(modifier = Modifier.height(20.dp))

            // Agua
            Text(
                "¿Cuántos vasos de agua has tomado hoy?",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Grilla de vasos
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                userScrollEnabled = false
            ) {
                items((1..cantidadVasos).toList()) { vaso ->
                    val seleccionado = vaso <= vasosConsumidos
                    IconButton(
                        onClick = {
                            val nuevaCantidad = if (vaso == vasosConsumidos) 0 else vaso
                            viewModel.seleccionarCantidadVasos(nuevaCantidad)
                            viewModel.registrarAgua()
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                color = if (seleccionado) colorSeleccionado else colorNoSeleccionado,
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalDrink,
                            contentDescription = null,
                            tint = if (seleccionado) iconTintSeleccionado else iconTintNoSeleccionado
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Total de agua
            Text(
                "Total consumido: ${vasosConsumidos * 250} ml",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            // Cargando...
            if (estadoCarga) {
                CircularProgressIndicator(
                    Modifier.padding(top = 16.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewInicioScreen() {
    val navController = rememberNavController()
    InicioScreen(navController = navController)
}
