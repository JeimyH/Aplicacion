package com.example.frontendproyectoapp.screen

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.frontendproyectoapp.model.Alimento
import com.example.frontendproyectoapp.viewModel.AlimentoViewModel
import com.example.frontendproyectoapp.viewModel.AlimentoViewModelFactory

@Composable
fun AlimentosFavoritosScreen(navController: NavHostController) {
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val viewModel: AlimentoViewModel = viewModel(
        factory = AlimentoViewModelFactory(application)
    )

    AlimentosFavoritosScreenContent(
        viewModel = viewModel,
        onBackClick = { navController.popBackStack() },
        navController = navController
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlimentosFavoritosScreenContent(
    viewModel: AlimentoViewModel,
    onBackClick: () -> Unit = {},
    navController: NavHostController
) {

    // Cargar favoritos al abrir
    LaunchedEffect(Unit) {
        viewModel.cargarDatos()
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Alimentos Favoritos",
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
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
                    AlimentoFavoritoItem(
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

@Composable
fun AlimentoFavoritoItem(
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
                            imageVector = Icons.Default.Delete,
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
fun PreviewFavoritosScreen() {
    val navController = rememberNavController()
    AlimentosFavoritosScreen(navController = navController)
}

