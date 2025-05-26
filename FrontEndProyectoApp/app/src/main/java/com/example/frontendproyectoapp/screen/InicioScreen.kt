package com.example.frontendproyectoapp.screen

import android.app.Application
import android.util.Log
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
import androidx.compose.ui.graphics.Color
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

    val context = LocalContext.current
    val idUsuarioFlow = produceState<Long?>(initialValue = null) {
        UserPreferences.obtenerIdUsuario(context).collect {
            value = it
        }
    }

    val cantidadVasos = 8
    val vasosConsumidos = viewModel.vasosConsumidosHoy
    val estadoCarga = viewModel.estadoCarga

    // Cargar el registro del usuario actual cada vez que cambia
    LaunchedEffect(idUsuarioFlow.value) {
        viewModel.cargarDatosUsuarioActual()
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Engranaje configuración
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = {
                    navController.navigate("ajustes")
                }) {
                    Icon(Icons.Default.Settings, contentDescription = "Ir a ajustes")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            CustomCalendar(
                selectedDate = selectedDate,
                onDateSelected = { selectedDate = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Calorías Totales / Calorías Recomendadas",
                style = MaterialTheme.typography.bodyMedium
            )
            Text("Kcal", style = MaterialTheme.typography.bodySmall)

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

            Text("¿Cuántos vasos de agua has tomado hoy?", style = MaterialTheme.typography.titleMedium)

            Spacer(Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp), // ajusta si necesitas más espacio
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                userScrollEnabled = false
            ) {
                items((1..cantidadVasos).toList()) { vaso ->
                    val seleccionado = vaso <= vasosConsumidos
                    IconButton(
                        onClick = {
                            viewModel.seleccionarCantidadVasos(vaso)
                            viewModel.registrarAgua()
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                if (seleccionado) Color(0xFF64B5F6) else Color(0xFFE0E0E0),
                                shape = CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalDrink,
                            contentDescription = null,
                            tint = if (seleccionado) Color.White else Color.Black
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Text("Total consumido: ${vasosConsumidos * 250} ml", style = MaterialTheme.typography.bodyLarge)

            if (estadoCarga) {
                CircularProgressIndicator(Modifier.padding(top = 16.dp))
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
