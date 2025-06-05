package com.example.frontendproyectoapp.screen

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
/*
@Composable
fun RutinaScreen(navController: NavHostController) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val viewModel: RutinaViewModel = viewModel(
        factory = RutinaViewModelFactory(application)
    )
    RutinaScreenContent(viewModel = viewModel, navController = navController)
}

@Composable
fun RutinaScreenContent(
    viewModel: RutinaViewModel,
    navController: NavHostController
) {
    val rutinaUiState by viewModel.rutinaState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 56.dp) // espacio para bottom nav
    ) {
        // Barra de búsqueda (navega al hacer click)
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Buscar") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable { navController.navigate("buscarAlimentos") },
            enabled = false,
            readOnly = true
        )

        // Tabs
        var selectedTabIndex by remember { mutableStateOf(0) }
        val tabs = listOf("Para ti", "Favoritos")
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }

        // Lista de comidas por momento del día
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            rutinaUiState.alimentosPorMomento.forEach { (momento, alimentos) ->
                item {
                    Text(
                        text = momento,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                items(alimentos) { alimento ->
                    AlimentoItem(alimento)
                }
            }
        }
    }

    // Bottom Navigation controlada por NavController
    BottomNavigationBar(navController = navController)
}

 */
