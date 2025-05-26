package com.example.frontendproyectoapp.screen

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.frontendproyectoapp.model.Alimento
import com.example.frontendproyectoapp.viewModel.BuscarAlimentoViewModel
import com.example.frontendproyectoapp.viewModel.BuscarAlimentoViewModelFactory


@Composable
fun BuscarAlimentoScreen(navController: NavHostController) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val viewModel: BuscarAlimentoViewModel = viewModel(
        factory = BuscarAlimentoViewModelFactory(application)
    )
    BuscarAlimentoScreenContent(viewModel = viewModel, navController = navController)
}


@Composable
fun BuscarAlimentoScreenContent(viewModel: BuscarAlimentoViewModel, navController: NavHostController) {
    val mostrarBaseDatos = remember { mutableStateOf(false) }

    Scaffold(bottomBar = { BottomNavigationBar(navController) }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Barra de búsqueda
            OutlinedTextField(
                value = viewModel.busqueda,
                onValueChange = {
                    viewModel.buscarEnTiempoReal(it)
                    if (it.isNotBlank()) {
                        mostrarBaseDatos.value = false
                    }
                },
                placeholder = { Text("Buscar alimento") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Botones: Base de datos / Favoritos
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = {
                    viewModel.cargarDatos()
                    mostrarBaseDatos.value = true
                }) {
                    Icon(Icons.Default.RestaurantMenu, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Base de Datos")
                }

                TextButton(onClick = {
                    navController.navigate("favoritos")
                }) {
                    Icon(Icons.Default.Favorite, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Favoritos")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (mostrarBaseDatos.value) {
                // Mostrar todos los alimentos
                viewModel.listaAlimentos.forEach { alimento ->
                    AlimentoItem(
                        alimento = alimento,
                        esFavorito = viewModel.esFavorito(alimento.idAlimento),
                        onClick = {
                            viewModel.agregarARecientes(alimento)
                            navController.navigate("detalleAlimento/${alimento.idAlimento}")
                        },
                        onToggleFavorito = {
                            viewModel.toggleFavorito(alimento)
                        }
                    )
                }
            } else {
                // Alimentos Recientes
                if (viewModel.alimentosRecientes.isNotEmpty()) {
                    Text("Alimentos Recientes", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    viewModel.alimentosRecientes.forEach { alimento ->
                        AlimentoItem(
                            alimento = alimento,
                            esFavorito = viewModel.esFavorito(alimento.idAlimento),
                            onClick = {
                                navController.navigate("detalleAlimento/${alimento.idAlimento}")
                            },
                            onToggleFavorito = {
                                viewModel.toggleFavorito(alimento)
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // Comidas Recientes
                if (viewModel.comidasRecientes.isNotEmpty()) {
                    Text("Comidas Recientes", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    viewModel.comidasRecientes.forEach { registro ->
                        AlimentoItem(
                            alimento = registro.alimento,
                            esFavorito = viewModel.esFavorito(registro.alimento.idAlimento),
                            onClick = {
                                navController.navigate("detalleAlimento/${registro.alimento.idAlimento}")
                            },
                            onToggleFavorito = {
                                viewModel.toggleFavorito(registro.alimento)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AlimentoItem(alimento: Alimento, onFavoritoClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .background(Color(0xFFF1F1F1), shape = RoundedCornerShape(8.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(alimento.nombreAlimento, style = MaterialTheme.typography.bodyLarge)
            Text(alimento.categoria, style = MaterialTheme.typography.bodySmall)
        }
        IconButton(onClick = onFavoritoClick) {
            Icon(Icons.Default.FavoriteBorder, contentDescription = "Marcar favorito")
        }
    }
}
@Composable
fun AlimentoItem(
    alimento: Alimento,
    esFavorito: Boolean,
    onClick: () -> Unit,
    onToggleFavorito: () -> Unit,
    onEliminarFavorito: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(alimento.nombreAlimento, style = MaterialTheme.typography.titleMedium)
                Text(" ${alimento.categoria}", style = MaterialTheme.typography.bodySmall)
            }

            Row {
                IconButton(onClick = { onToggleFavorito() }) {
                    Icon(
                        imageVector = if (esFavorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorito",
                        tint = if (esFavorito) Color.Red else Color.Gray
                    )
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



