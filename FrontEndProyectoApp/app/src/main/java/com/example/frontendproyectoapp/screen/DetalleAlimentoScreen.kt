package com.example.frontendproyectoapp.screen

import android.app.Application
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.frontendproyectoapp.interfaces.RetrofitClientAlimento
import com.example.frontendproyectoapp.model.Alimento
import com.example.frontendproyectoapp.viewModel.DetalleAlimentoViewModel
import com.example.frontendproyectoapp.viewModel.DetalleAlimentoViewModelFactory
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleAlimentoScreen(idAlimento: Long, navController: NavHostController) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val viewModel: DetalleAlimentoViewModel = viewModel(
        factory = DetalleAlimentoViewModelFactory(application)
    )

    val alimentoState = remember { mutableStateOf<Alimento?>(null) }

    LaunchedEffect(idAlimento) {
        try {
            val alimento = RetrofitClientAlimento.alimentoService.listarIdAlimento(idAlimento)
            alimentoState.value = alimento
        } catch (e: Exception) {
            Log.e("DetalleAlimento", "Error: ${e.message}")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Alimento") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { innerPadding ->
        DetalleAlimentoScreenContent(
            alimento = alimentoState.value,
            navController = navController,
            viewModel = viewModel,
            paddingValues = innerPadding
        )
    }
}


@Composable
fun DetalleAlimentoScreenContent(
    alimento: Alimento?,
    navController: NavHostController,
    viewModel: DetalleAlimentoViewModel,
    paddingValues: PaddingValues
) {
    var cantidad by remember { mutableStateOf(1f) }
    var unidadSeleccionada by remember { mutableStateOf("unidad") }
    var momentoSeleccionado by remember { mutableStateOf("Cena") }

    val unidades = listOf("unidad", "gramos")
    val momentos = listOf("Desayuno", "Almuerzo", "Cena", "Snack")

    if (alimento == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val totalCalorias = (alimento.calorias * cantidad).roundToInt()
    val totalProteinas = alimento.proteinas * cantidad
    val totalCarbs = alimento.carbohidratos * cantidad
    val totalGrasas = alimento.grasas * cantidad

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(alimento.nombreAlimento, style = MaterialTheme.typography.titleLarge)
        Text("Genérico", color = Color.Gray)

        Spacer(Modifier.height(16.dp))
        Text("Datos por 1 ${alimento.unidadBase} (${alimento.cantidadBase}g)", color = Color.Gray)

        Spacer(Modifier.height(16.dp))
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.CenterHorizontally)
                .border(8.dp, Color(0xFFFFC107), CircleShape)
        ) {
            Text("$totalCalorias kcal", fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            NutrienteTag("Proteínas", totalProteinas, Color(0xFFFFC107))
            NutrienteTag("Carbs", totalCarbs, Color(0xFF8BC34A))
            NutrienteTag("Grasas", totalGrasas, Color(0xFFFF9800))
        }

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = cantidad.toString(),
            onValueChange = { cantidad = it.toFloatOrNull() ?: 1f },
            label = { Text("Cantidad") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))
        DropdownMenuWithLabel("Porción", unidades, unidadSeleccionada) {
            unidadSeleccionada = it
        }

        Spacer(Modifier.height(8.dp))
        DropdownMenuWithLabel("Momento del día", momentos, momentoSeleccionado) {
            momentoSeleccionado = it
        }

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                viewModel.registrarAlimento(
                    idAlimento = alimento.idAlimento,
                    cantidad = cantidad,
                    unidad = unidadSeleccionada,
                    momento = momentoSeleccionado
                )
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth(),
            //colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107))
        ) {
            Text("Ingresar a $momentoSeleccionado")
        }
    }
}

@Composable
fun NutrienteTag(nombre: String, valor: Float, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(color, shape = CircleShape)
        )
        Spacer(Modifier.height(4.dp))
        Text(nombre, style = MaterialTheme.typography.bodySmall)
        Text("${"%.1f".format(valor)} g", style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun DropdownMenuWithLabel(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(label, style = MaterialTheme.typography.bodySmall)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .padding(12.dp)
        ) {
            Text(selectedOption)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}
