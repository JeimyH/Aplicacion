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

    // Cargar favoritos al entrar a la pantalla
    LaunchedEffect(Unit) {
        viewModel.cargarDatos()
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Alimentos Favoritos") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (viewModel.favoritos.isEmpty()) {
                item {
                    Text("No tienes alimentos favoritos aún.")
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

