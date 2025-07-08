package com.example.frontendproyectoapp.screen

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.frontendproyectoapp.viewModel.BuscarAlimentoViewModel
import com.example.frontendproyectoapp.viewModel.BuscarAlimentoViewModelFactory

@Composable
fun AlimentosFavoritosScreen(navController: NavHostController) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val viewModel: BuscarAlimentoViewModel = viewModel(
        factory = BuscarAlimentoViewModelFactory(application)
    )

    AlimentosFavoritosScreenContent(viewModel = viewModel, navController = navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlimentosFavoritosScreenContent(viewModel: BuscarAlimentoViewModel, navController: NavHostController) {
    val context = LocalContext.current

    // Cargar favoritos al abrir
    LaunchedEffect(Unit) {
        viewModel.cargarDatos()
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(
                        "Alimentos Favoritos",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Atrás",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            if (viewModel.favoritos.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No tienes alimentos favoritos aún.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                items(viewModel.favoritos) { alimento ->
                    AlimentoItem(
                        alimento = alimento,
                        esFavorito = true,
                        onClick = {
                            navController.navigate("detalleAlimento/${alimento.idAlimento}")
                        },
                        onToggleFavorito = {
                            viewModel.toggleFavorito(alimento)
                        },
                        onEliminarFavorito = {
                            viewModel.toggleFavorito(alimento)
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewFavoritosScreen() {
    val navController = rememberNavController()
    AlimentosFavoritosScreen(navController = navController)
}

