package com.example.frontendproyectoapp.screen

import androidx.compose.foundation.Image
import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.RestaurantMenu
import androidx.compose.material.icons.rounded.Search
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
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
    val viewModel: BuscarAlimentoViewModel = viewModel(factory = BuscarAlimentoViewModelFactory(application))

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val lifecycle = currentBackStackEntry?.lifecycle

    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.actualizarUsuarioYDatos()
            }
        }
        lifecycle?.addObserver(observer)
        onDispose { lifecycle?.removeObserver(observer) }
    }

    BuscarAlimentoScreenContent(viewModel, navController)
}

@Composable
fun BuscarAlimentoScreenContent(viewModel: BuscarAlimentoViewModel, navController: NavHostController) {
    val mostrarBaseDatos = remember { mutableStateOf(false) }
    val isSearchFocused = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { focusManager.clearFocus() },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Búsqueda
            BarraBusqueda(viewModel, mostrarBaseDatos, isSearchFocused)

            Spacer(Modifier.height(6.dp))

            // Botones superiores (base de datos / favoritos)
            BotonesSuperiores(viewModel, navController, mostrarBaseDatos)

            Spacer(modifier = Modifier.height(6.dp))

            // Contenido
            when {
                viewModel.errorCarga != null -> MostrarError(viewModel)
                viewModel.busqueda.isNotBlank() && isSearchFocused.value -> MostrarResultadosBusqueda(
                    viewModel,
                    navController
                )

                mostrarBaseDatos.value -> MostrarBaseDeDatos(viewModel, navController)
                else -> {
                    SeccionAlimentosRecientes(viewModel, navController)
                    SeccionConsumoDiario(viewModel, navController)
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
        placeholder = { Text("Buscar alimento", color = MaterialTheme.colorScheme.onSurfaceVariant) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = "Buscar",
                tint = MaterialTheme.colorScheme.primary
            )
        },
        trailingIcon = {
            if (viewModel.busqueda.isNotBlank()) {
                IconButton(onClick = {
                    viewModel.busqueda = ""
                    focusManager.clearFocus()
                    mostrarBaseDatos.value = false
                }) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Borrar búsqueda",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onFocusChanged { isSearchFocused.value = it.isFocused }
            .padding(vertical = 8.dp),
        singleLine = true,
        shape = RoundedCornerShape(20.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            cursorColor = MaterialTheme.colorScheme.primary
        )
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Botón "Base de Datos"
        Button(
            onClick = {
                focusManager.clearFocus()
                mostrarBaseDatos.value = !mostrarBaseDatos.value
                if (mostrarBaseDatos.value) viewModel.cargarDatos()
            },
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary, // PastelLeaf
                contentColor = MaterialTheme.colorScheme.onSecondary // White en claro
            ),
            elevation = ButtonDefaults.buttonElevation(4.dp)
        ) {
            Icon(Icons.Rounded.RestaurantMenu, contentDescription = null)
            Spacer(Modifier.width(6.dp))
            Text(if (mostrarBaseDatos.value) "Cerrar Base de Datos" else "Base de Datos")
        }

        // Botón "Favoritos"
        OutlinedButton(
            onClick = { navController.navigate("favoritos") },
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.primary // PastelMint
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
        ) {
            Icon(Icons.Rounded.Favorite, contentDescription = "Favoritos")
            Spacer(Modifier.width(6.dp))
            Text("Favoritos")
        }
    }

    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun MostrarBaseDeDatos(viewModel: BuscarAlimentoViewModel, navController: NavHostController) {
    Text(
        text = "Base de Datos de Alimentos",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 8.dp)
    )

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
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Alimentos Recientes",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onBackground // ← Pastel coherente
        )

        TextButton(
            onClick = { viewModel.eliminarTodosRecientes() },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.textButtonColors(
                contentColor = MaterialTheme.colorScheme.error // ← PastelError
            )
        ) {
            Icon(Icons.Rounded.Delete, contentDescription = "Eliminar todos")
            Spacer(Modifier.width(6.dp))
            Text("Eliminar todos")
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    if (viewModel.alimentosRecientes.isEmpty()) {
        Text(
            text = "Aún no has buscado ningún alimento.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant // ← suave y legible
        )
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
                onEliminarFavorito = { viewModel.eliminarRecienteIndividual(alimento.idAlimento) }
            )
        }
    }

    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
fun SeccionConsumoDiario(viewModel: BuscarAlimentoViewModel, navController: NavHostController) {
    Text(
        text = "Resumen de Consumo Diario",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(start = 8.dp, top = 8.dp, bottom = 12.dp)
    )

    val comidasAgrupadas = viewModel.comidasRecientes.groupBy { it.momentoDelDia }
    val momentos = listOf("Desayuno", "Almuerzo", "Cena", "Snack")
    var registroAEliminar by remember { mutableStateOf<RegistroAlimentoSalida?>(null) }

    momentos.forEach { momento ->
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = momento,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { viewModel.eliminarRegistrosPorMomentoYFecha(momento) },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Eliminar",
                                tint = MaterialTheme.colorScheme.error,
                                //modifier = Modifier.size(20.dp)
                            )
                        }

                        IconButton(
                            onClick = {
                                viewModel.mostrarDialogoRegistro.value = true
                                viewModel.momentoSeleccionado = momento
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Agregar",
                                tint = MaterialTheme.colorScheme.primary,
                                //modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                val registros = comidasAgrupadas[momento].orEmpty()

                if (registros.isEmpty()) {
                    Text(
                        text = "No has registrado los alimentos para $momento.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
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
                                registroAEliminar = registro
                            },
                            mostrarFavorito = false
                        )
                    }
                }
            }
        }
    }

    DialogoRegistroAlimento(viewModel)

    // Diálogo de confirmación
    registroAEliminar?.let {
        AlertDialog(
            onDismissRequest = { registroAEliminar = null },
            title = { Text("Eliminar alimento") },
            text = { Text("¿Estás seguro de que deseas eliminar este alimento del registro?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.eliminarRegistroIndividual(it.idRegistroAlimento)
                    registroAEliminar = null
                }) {
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

// Diálogo de registro adaptado visualmente
@Composable
fun DialogoRegistroAlimento(viewModel: BuscarAlimentoViewModel) {
    if (!viewModel.mostrarDialogoRegistro.value) return

    val contexto = LocalContext.current
    val listaAlimentos = viewModel.listaAlimentos
    val unidadesDeMedida = listOf(
        "mg", "g", "kg", "ml", "l", "tsp", "tbsp", "cup", "oz", "lb",
        "unidad", "porción", "rebanada", "pieza", "taza", "vaso", "lonja", "filete", "puñado"
    )

    var alimentoSeleccionado by remember { mutableStateOf<Alimento?>(null) }
    var cantidadTexto by remember { mutableStateOf("") }
    var unidadTexto by remember { mutableStateOf("") }

    var alimentoExpanded by remember { mutableStateOf(false) }
    var unidadExpanded by remember { mutableStateOf(false) }

    var alimentoFieldPx by remember { mutableStateOf(0) }
    var unidadFieldPx by remember { mutableStateOf(0) }
    val density = LocalDensity.current

    AlertDialog(
        onDismissRequest = {
            viewModel.mostrarDialogoRegistro.value = false
            alimentoSeleccionado = null
            cantidadTexto = ""
            unidadTexto = ""
        },
        title = {
            Text("Registrar Alimento", style = MaterialTheme.typography.titleMedium)
        },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 8.dp)
            ) {
                // Selector de alimento
                Box {
                    OutlinedTextField(
                        value = alimentoSeleccionado?.nombreAlimento ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Selecciona un alimento") },
                        trailingIcon = {
                            IconButton(onClick = { alimentoExpanded = !alimentoExpanded }) {
                                Icon(
                                    if (alimentoExpanded) Icons.Default.KeyboardArrowUp
                                    else Icons.Default.KeyboardArrowDown,
                                    contentDescription = null
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onGloballyPositioned {
                                alimentoFieldPx = it.size.width
                            }
                    )

                    DropdownMenu(
                        expanded = alimentoExpanded,
                        onDismissRequest = { alimentoExpanded = false },
                        modifier = Modifier
                            .width(with(density) { alimentoFieldPx.toDp() })
                            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(12.dp))
                    ) {
                        listaAlimentos.forEach { alimento ->
                            DropdownMenuItem(
                                text = { Text(alimento.nombreAlimento) },
                                onClick = {
                                    alimentoSeleccionado = alimento
                                    alimentoExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Cantidad
                OutlinedTextField(
                    value = cantidadTexto,
                    onValueChange = { cantidadTexto = it },
                    label = { Text("Cantidad") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Box {
                    OutlinedTextField(
                        value = unidadTexto,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Unidad de medida") },
                        trailingIcon = {
                            IconButton(onClick = { unidadExpanded = !unidadExpanded }) {
                                Icon(
                                    if (unidadExpanded) Icons.Default.KeyboardArrowUp
                                    else Icons.Default.KeyboardArrowDown,
                                    contentDescription = null
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { unidadExpanded = true }
                            .onGloballyPositioned {
                                unidadFieldPx = it.size.width
                            }
                    )

                    DropdownMenu(
                        expanded = unidadExpanded,
                        onDismissRequest = { unidadExpanded = false },
                        modifier = Modifier
                            .width(with(density) { unidadFieldPx.toDp() })
                            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(12.dp))
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
            TextButton(onClick = {
                val cantidad = cantidadTexto.toFloatOrNull()
                if (alimentoSeleccionado != null && unidadTexto.isNotBlank() && cantidad != null) {
                    viewModel.registrarAlimentoDesdeDialogo(
                        idAlimento = alimentoSeleccionado!!.idAlimento,
                        cantidad = cantidad,
                        unidad = unidadTexto,
                        momento = viewModel.momentoSeleccionado
                    )
                    viewModel.mostrarDialogoRegistro.value = false
                    alimentoSeleccionado = null
                    cantidadTexto = ""
                    unidadTexto = ""
                    Toast.makeText(contexto, "Alimento registrado con éxito", Toast.LENGTH_SHORT).show()
                    viewModel.cargarComidasRecientes()
                } else {
                    Toast.makeText(contexto, "Completa todos los campos correctamente", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text("Registrar", color = MaterialTheme.colorScheme.primary)
            }
        },
        dismissButton = {
            TextButton(onClick = {
                viewModel.mostrarDialogoRegistro.value = false
            }) {
                Text("Cancelar", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        },
        shape = RoundedCornerShape(16.dp),
        containerColor = MaterialTheme.colorScheme.surface
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
        ElevatedCard(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            ),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(0.9f)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = viewModel.errorCarga ?: "Ha ocurrido un error inesperado.",
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                FilledTonalButton(
                    onClick = {
                        viewModel.errorCarga = null
                        viewModel.cargarDatos()
                    },
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text("Reintentar", color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 3.dp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Cargando alimentos...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun MostrarResultadosBusqueda(viewModel: BuscarAlimentoViewModel, navController: NavHostController) {
    Text(
        text = "Resultados para \"${viewModel.busqueda}\"",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
        color = MaterialTheme.colorScheme.primary
    )
    Spacer(modifier = Modifier.height(12.dp))

    if (viewModel.alimentosFiltrados.isEmpty()) {
        Text(
            text = "No se encontraron coincidencias.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
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
    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
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
            // Imagen del alimento
            Image(
                painter = rememberAsyncImagePainter(alimento.urlImagen),
                contentDescription = "Imagen del alimento",
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = alimento.nombreAlimento,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = alimento.categoria,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row {
                if (mostrarFavorito) {
                    IconButton(onClick = onToggleFavorito) {
                        Icon(
                            imageVector = if (esFavorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorito",
                            tint = if (esFavorito)
                                MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.outline
                        )
                    }
                }

                onEliminarFavorito?.let {
                    IconButton(onClick = it) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Eliminar",
                            tint = MaterialTheme.colorScheme.error
                        )
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

