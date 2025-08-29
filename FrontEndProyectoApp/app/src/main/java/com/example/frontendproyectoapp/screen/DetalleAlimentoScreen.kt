package com.example.frontendproyectoapp.screen

import android.app.Application
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.frontendproyectoapp.interfaces.AlimentoService
import com.example.frontendproyectoapp.interfaces.RetrofitClient
import com.example.frontendproyectoapp.model.Alimento
import com.example.frontendproyectoapp.viewModel.AlimentoViewModel
import com.example.frontendproyectoapp.viewModel.AlimentoViewModelFactory
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
    val alimentoViewModel: AlimentoViewModel = viewModel(factory = AlimentoViewModelFactory(application))

    val alimentoService = RetrofitClient.createService(AlimentoService::class.java)
    val alimentoState = remember { mutableStateOf<Alimento?>(null) }

    LaunchedEffect(idAlimento) {
        try {
            val alimento = alimentoService.listarIdAlimento(idAlimento)
            alimentoState.value = alimento
            alimento?.let { alimentoViewModel.cargarUnidadesPorId(it.idAlimento) }
        } catch (e: Exception) {
            Log.e("DetalleAlimento", "Error: ${e.message}")
        }
    }

    DetalleAlimentoScreenContent(
        alimento = alimentoState.value,
        navController = navController,
        viewModel = viewModel,
        onBackClick = { navController.popBackStack() },
        alimentoViewModel = alimentoViewModel
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleAlimentoScreenContent(
    alimento: Alimento?,
    navController: NavHostController,
    viewModel: DetalleAlimentoViewModel,
    onBackClick: () -> Unit = {},
    alimentoViewModel: AlimentoViewModel
) {
    val focusManager = LocalFocusManager.current
    var cantidad by remember { mutableStateOf("1") }
    var unidadSeleccionada by remember { mutableStateOf("Unidad de medida") }
    var unidadExpanded by remember { mutableStateOf(false) }

    var momentoSeleccionado by remember { mutableStateOf("Momento del día") }
    var momentoExpanded by remember { mutableStateOf(false) }

    val momentos = listOf("Desayuno", "Almuerzo", "Cena", "Snack")
    val unidadesDisponibles by alimentoViewModel.unidades

    if (alimento == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        return
    }

    val cantidadFloat = cantidad.toFloatOrNull() ?: 1f
    val totalCalorias = (alimento.calorias * cantidadFloat).roundToInt()
    val totalProteinas = alimento.proteinas * cantidadFloat
    val totalCarbs = alimento.carbohidratos * cantidadFloat
    val totalGrasas = alimento.grasas * cantidadFloat

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
        ) {
            Text(alimento.nombreAlimento, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground)
            Text("Genérico", color = MaterialTheme.colorScheme.onSurfaceVariant)

            Spacer(Modifier.height(16.dp))
            Text("Datos por 100 ${alimento.unidadBase} (${alimento.cantidadBase}g)", color = MaterialTheme.colorScheme.onSurfaceVariant)

            Spacer(Modifier.height(16.dp))
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally)
                    .border(8.dp, MaterialTheme.colorScheme.primary, CircleShape)
            ) {
                Text("$totalCalorias kcal", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            }

            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                NutrienteTag("Proteínas", totalProteinas, MaterialTheme.colorScheme.primary)
                NutrienteTag("Carbs", totalCarbs, MaterialTheme.colorScheme.secondary)
                NutrienteTag("Grasas", totalGrasas, MaterialTheme.colorScheme.tertiary)
            }

            Spacer(Modifier.height(24.dp))

            // TextField cantidad estilo Card + borde de card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                BasicTextField(
                    value = cantidad,
                    onValueChange = { input -> cantidad = input.filter { it.isDigit() || it == '.' } },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                ) { innerTextField ->
                    Box {
                        if (cantidad.isEmpty()) {
                            Text("Cantidad", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        innerTextField()
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            DropdownSelector(
                label = "Unidad de medida",
                selected = unidadSeleccionada,
                options = unidadesDisponibles,
                expanded = unidadExpanded,
                onExpandedChange = { unidadExpanded = it },
                onItemSelected = {
                    unidadSeleccionada = it
                    unidadExpanded = false
                }
            )

            Spacer(Modifier.height(8.dp))

            DropdownSelector(
                label = "Momento del día",
                selected = momentoSeleccionado,
                options = momentos,
                expanded = momentoExpanded,
                onExpandedChange = { momentoExpanded = it },
                onItemSelected = {
                    momentoSeleccionado = it
                    momentoExpanded = false
                }
            )

            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    if (unidadSeleccionada.isNotBlank() && cantidadFloat > 0f) {
                        viewModel.registrarAlimento(
                            idAlimento = alimento.idAlimento,
                            cantidad = cantidadFloat,
                            unidad = unidadSeleccionada,
                            momento = momentoSeleccionado
                        )
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Ingresar a $momentoSeleccionado", color = MaterialTheme.colorScheme.onPrimary)
            }
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