package com.example.frontendproyectoapp.screen

import androidx.compose.foundation.Image
import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.frontendproyectoapp.model.Alimento
import com.example.frontendproyectoapp.model.RegistroAlimentoSalida
import com.example.frontendproyectoapp.viewModel.BuscarAlimentoViewModel
import com.example.frontendproyectoapp.viewModel.BuscarAlimentoViewModelFactory

// Pantalla principal de búsqueda de alimentos
@Composable
fun BuscarAlimentoScreen(navController: NavHostController) {
    val context = LocalContext.current
    val application = context.applicationContext as Application

    // Se obtiene el ViewModel con su fábrica personalizada
    val viewModel: BuscarAlimentoViewModel = viewModel(factory = BuscarAlimentoViewModelFactory(application))

    // Detecta cambios en el ciclo de vida de la pantalla
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val lifecycle = currentBackStackEntry?.lifecycle

    // Se usa DisposableEffect para ejecutar lógica cuando el screen vuelve a estar activo
    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                // Se actualizan los datos del usuario al volver a la pantalla
                viewModel.actualizarUsuarioYDatos()
            }
        }
        lifecycle?.addObserver(observer)
        onDispose { lifecycle?.removeObserver(observer) } // Limpia el observer al salir
    }

    // Llama al contenido visual del screen
    BuscarAlimentoScreenContent(viewModel, navController)
}

// Contenido principal del screen
@Composable
fun BuscarAlimentoScreenContent(
    viewModel: BuscarAlimentoViewModel,
    navController: NavHostController
) {
    val mostrarBaseDatos = remember { mutableStateOf(false) } // Si se muestra la base de datos completa
    val isSearchFocused = remember { mutableStateOf(false) } // Si el campo de búsqueda tiene foco
    val focusManager = LocalFocusManager.current // Para manejar el enfoque de teclado

    Scaffold(bottomBar = { BottomNavigationBar(navController) }) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { focusManager.clearFocus()} // Quita el foco al tocar fuera
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                BarraBusqueda(viewModel, mostrarBaseDatos, isSearchFocused)
                BotonesSuperiores(viewModel, navController, mostrarBaseDatos)

                // Muestra errores o loading según el estado del ViewModel
                if (viewModel.errorCarga != null) {
                    MostrarError(viewModel)
                } else if (viewModel.listaAlimentos.isEmpty() && viewModel.busqueda.isBlank() && !mostrarBaseDatos.value) {
                    MostrarCargando()
                } else {
                    when {
                        viewModel.busqueda.isNotBlank() && isSearchFocused.value -> {
                            MostrarResultadosBusqueda(viewModel, navController)
                        }

                        mostrarBaseDatos.value -> {
                            MostrarBaseDeDatos(viewModel, navController)
                        }

                        else -> {
                            SeccionAlimentosRecientes(viewModel, navController)
                            SeccionConsumoDiario(viewModel, navController)
                        }
                    }
                }
            }
        }
    }
}

// Campo de texto para buscar alimentos
@Composable
fun BarraBusqueda(
    viewModel: BuscarAlimentoViewModel,
    mostrarBaseDatos: MutableState<Boolean>,
    isSearchFocused: MutableState<Boolean>
) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = viewModel.busqueda,
        onValueChange = {
            viewModel.buscarEnTiempoReal(it)
            if (it.isBlank()) mostrarBaseDatos.value = false
        },
        placeholder = { Text("Buscar alimento") },
        leadingIcon = { Icon(Icons.Default.Search, null) },
        trailingIcon = {
            if (viewModel.busqueda.isNotBlank()) {
                IconButton(onClick = {
                    viewModel.busqueda = ""
                    focusManager.clearFocus()
                    mostrarBaseDatos.value = false
                }) {
                    Icon(Icons.Default.Close, null)
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onFocusChanged { isSearchFocused.value = it.isFocused }
    )
}

@Composable
fun BotonesSuperiores(
    viewModel: BuscarAlimentoViewModel,
    navController: NavHostController,
    mostrarBaseDatos: MutableState<Boolean>
) {
    val focusManager = LocalFocusManager.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextButton(onClick = {
            focusManager.clearFocus()
            mostrarBaseDatos.value = !mostrarBaseDatos.value
            if (mostrarBaseDatos.value) viewModel.cargarDatos()
        }) {
            Icon(Icons.Default.RestaurantMenu, null)
            Spacer(Modifier.width(4.dp))
            Text(if (mostrarBaseDatos.value) "Cerrar Base de Datos" else "Base de Datos")
        }

        TextButton(onClick = {
            navController.navigate("favoritos")
        }) {
            Icon(Icons.Default.Favorite, null)
            Spacer(Modifier.width(4.dp))
            Text("Favoritos")
        }
    }

    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun MostrarBaseDeDatos(viewModel: BuscarAlimentoViewModel, navController: NavHostController) {
    Text("Base de Datos de Alimentos", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(8.dp))

    viewModel.listaAlimentos.forEach { alimento ->
        AlimentoItem(
            alimento = alimento,
            esFavorito = viewModel.esFavorito(alimento.idAlimento),
            onClick = {
                viewModel.busqueda = ""
                viewModel.agregarARecientes(alimento)
                navController.navigate("detalleAlimento/${alimento.idAlimento}")
            },
            onToggleFavorito = { viewModel.toggleFavorito(alimento) }
        )
    }
}

@Composable
fun SeccionAlimentosRecientes(viewModel: BuscarAlimentoViewModel, navController: NavHostController) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Alimentos Recientes", style = MaterialTheme.typography.titleMedium)
        TextButton(onClick = { viewModel.eliminarTodosRecientes() }) {
            Icon(Icons.Default.Delete, null)
            Spacer(Modifier.width(4.dp))
            Text("Eliminar todos")
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    if (viewModel.alimentosRecientes.isEmpty()) {
        Text("Aún no has buscado ningún alimento.", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
    } else {
        viewModel.alimentosRecientes.forEach { alimento ->
            AlimentoItem(
                alimento = alimento,
                esFavorito = viewModel.esFavorito(alimento.idAlimento),
                onClick = {
                    viewModel.busqueda = ""
                    navController.navigate("detalleAlimento/${alimento.idAlimento}")
                },
                onToggleFavorito = { viewModel.toggleFavorito(alimento) },
                onEliminarFavorito = { viewModel.eliminarRecienteIndividual(alimento.idAlimento) },
                // mostrarFavorito = false  // ocultamos favoritos en esta sección
            )
        }
    }

    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
fun SeccionConsumoDiario(viewModel: BuscarAlimentoViewModel, navController: NavHostController) {
    Text("Resumen de Consumo Diario", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(8.dp))

    val comidasAgrupadas = viewModel.comidasRecientes.groupBy { it.momentoDelDia }
    val momentos = listOf("Desayuno", "Almuerzo", "Cena", "Snack")

    // Estado para mostrar diálogo de confirmación
    var registroAEliminar by remember { mutableStateOf<RegistroAlimentoSalida?>(null) }

    momentos.forEach { momento ->
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(momento, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            Row {
                TextButton(onClick = {
                    viewModel.eliminarRegistrosPorMomentoYFecha(momento)
                }) {
                    Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Gray)
                    Spacer(Modifier.width(4.dp))
                    Text("Eliminar")
                }

                TextButton(onClick = {
                    viewModel.mostrarDialogoRegistro.value = true
                    viewModel.momentoSeleccionado = momento
                }) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Agregar")
                }
            }
        }

        val registros = comidasAgrupadas[momento].orEmpty()

        if (registros.isEmpty()) {
            Text(
                "No has registrado los alimentos que consumiste para $momento.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        } else {
            registros.forEach { registro ->
                AlimentoItem(
                    alimento = registro.alimento,
                    esFavorito = viewModel.esFavorito(registro.alimento.idAlimento),
                    onClick = {
                        navController.navigate("detalleAlimento/${registro.alimento.idAlimento}")
                    },
                    onToggleFavorito = {
                        viewModel.toggleFavorito(registro.alimento)
                    },
                    onEliminarFavorito = {
                        registroAEliminar = registro // Abrir diálogo de confirmación
                    },
                    mostrarFavorito = false
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }

    DialogoRegistroAlimento(viewModel)

    // Diálogo de confirmación para eliminación individual
    if (registroAEliminar != null) {
        AlertDialog(
            onDismissRequest = { registroAEliminar = null },
            title = { Text("Eliminar alimento") },
            text = { Text("¿Estás seguro de que deseas eliminar este alimento del registro?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.eliminarRegistroIndividual(registroAEliminar!!.idRegistroAlimento)
                        registroAEliminar = null
                    }
                ) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { registroAEliminar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun DialogoRegistroAlimento(viewModel: BuscarAlimentoViewModel) {
    if (!viewModel.mostrarDialogoRegistro.value) return

    var alimentoSeleccionado by remember { mutableStateOf<Alimento?>(null) }
    var cantidadTexto by remember { mutableStateOf("") }

    // Estado para unidad de medida
    var unidadTexto by remember { mutableStateOf("") }
    var unidadExpanded by remember { mutableStateOf(false) }

    // Lista fija de unidades válidas
    val unidadesDeMedida = listOf(
        "mg", "g", "kg",
        "ml", "l",
        "tsp", "tbsp", "cup",
        "oz", "lb",
        "unidad", "porción", "rebanada", "pieza", "taza", "vaso", "lonja", "filete", "puñado"
    )

    val listaAlimentos = viewModel.listaAlimentos
    val contexto = LocalContext.current

    AlertDialog(
        onDismissRequest = {
            viewModel.mostrarDialogoRegistro.value = false
            alimentoSeleccionado = null
            cantidadTexto = ""
            unidadTexto = ""
        },
        title = { Text("Registrar Alimento") },
        text = {
            Column {
                var expanded by remember { mutableStateOf(false) }

                // Campo de selección de alimento
                OutlinedTextField(
                    value = alimentoSeleccionado?.nombreAlimento ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Selecciona un alimento") },
                    trailingIcon = {
                        IconButton(onClick = { expanded = !expanded }) {
                            Icon(
                                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = null
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    listaAlimentos.forEach { alimento ->
                        DropdownMenuItem(
                            text = { Text(alimento.nombreAlimento) },
                            onClick = {
                                alimentoSeleccionado = alimento
                                expanded = false
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Campo de cantidad
                OutlinedTextField(
                    value = cantidadTexto,
                    onValueChange = { cantidadTexto = it },
                    label = { Text("Cantidad") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Campo desplegable de unidad
                Text("Unidad de medida", style = MaterialTheme.typography.labelSmall)
                Box {
                    OutlinedTextField(
                        value = unidadTexto,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { unidadExpanded = true },
                        trailingIcon = {
                            IconButton(onClick = { unidadExpanded = !unidadExpanded }) {
                                Icon(
                                    imageVector = if (unidadExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                    contentDescription = null
                                )
                            }
                        }
                    )

                    DropdownMenu(
                        expanded = unidadExpanded,
                        onDismissRequest = { unidadExpanded = false }
                    ) {
                        unidadesDeMedida.forEach { unidad ->
                            DropdownMenuItem(
                                text = { Text(unidad) },
                                onClick = {
                                    unidadTexto = unidad
                                    unidadExpanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val cantidad = cantidadTexto.toFloatOrNull()
                    if (alimentoSeleccionado != null && unidadTexto.isNotBlank() && cantidad != null) {
                        viewModel.registrarAlimentoDesdeDialogo(
                            idAlimento = alimentoSeleccionado!!.idAlimento,
                            cantidad = cantidad,
                            unidad = unidadTexto,
                            momento = viewModel.momentoSeleccionado
                        )

                        // Cierra diálogo y limpia campos
                        viewModel.mostrarDialogoRegistro.value = false
                        alimentoSeleccionado = null
                        cantidadTexto = ""
                        unidadTexto = ""

                        // Notificación y recarga
                        Toast.makeText(contexto, "Alimento registrado con éxito", Toast.LENGTH_SHORT).show()
                        viewModel.cargarComidasRecientes()
                    } else {
                        Toast.makeText(contexto, "Completa todos los campos correctamente", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Text("Registrar")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                viewModel.mostrarDialogoRegistro.value = false
            }) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun MostrarError(viewModel: BuscarAlimentoViewModel) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Warning, contentDescription = null, tint = Color.Red)
            Spacer(modifier = Modifier.height(8.dp))
            Text(viewModel.errorCarga ?: "", color = Color.Red)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                viewModel.errorCarga = null
                viewModel.cargarDatos()
            }) {
                Text("Reintentar")
            }
        }
    }
}

@Composable
fun MostrarCargando() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(8.dp))
            Text("Cargando alimentos...")
        }
    }
}

@Composable
fun MostrarResultadosBusqueda(viewModel: BuscarAlimentoViewModel, navController: NavHostController) {
    Text(
        "Resultados para \"${viewModel.busqueda}\"",
        style = MaterialTheme.typography.titleMedium
    )
    Spacer(modifier = Modifier.height(8.dp))

    if (viewModel.alimentosFiltrados.isEmpty()) {
        Text("No se encontraron coincidencias.")
    } else {
        viewModel.alimentosFiltrados.forEach { alimento ->
            AlimentoItem(
                alimento = alimento,
                esFavorito = viewModel.esFavorito(alimento.idAlimento),
                onClick = {
                    viewModel.busqueda = ""
                    viewModel.agregarARecientes(alimento)
                    navController.navigate("detalleAlimento/${alimento.idAlimento}")
                },
                onToggleFavorito = { viewModel.toggleFavorito(alimento) }
            )
        }
    }
}


@Composable
fun AlimentoItem(
    alimento: Alimento,
    esFavorito: Boolean,
    onClick: () -> Unit,
    onToggleFavorito: () -> Unit,
    onEliminarFavorito: (() -> Unit)? = null,
    mostrarFavorito: Boolean = true
) {
    Card(
        // Borde redondeado personalizado
        shape = RoundedCornerShape(12.dp),
        // Elevación personalizada (sombra)
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        // Color de fondo personalizado
        colors = CardDefaults.cardColors(containerColor = Color.White),
        // Borde con color
        border = BorderStroke(0.5.dp, Color.LightGray),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen del alimento sin fondo adicional
            Image(
                painter = rememberAsyncImagePainter(alimento.urlImagen),
                contentDescription = "Imagen del alimento",
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Nombre y categoría del alimento
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(alimento.nombreAlimento, style = MaterialTheme.typography.titleMedium)
                Text(alimento.categoria, style = MaterialTheme.typography.bodySmall)
            }

            // Botones de acción
            Row {
                if (mostrarFavorito) {
                    IconButton(onClick = onToggleFavorito) {
                        Icon(
                            imageVector = if (esFavorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorito",
                            tint = if (esFavorito) Color.Red else Color.Gray
                        )
                    }
                }

                if (onEliminarFavorito != null) {
                    IconButton(onClick = onEliminarFavorito) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewBuscarAlimentoScreen() {
    val navController = rememberNavController()
    BuscarAlimentoScreen(navController = navController)
}

