package com.example.frontendproyectoapp.screen

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.frontendproyectoapp.model.Alimento
import com.example.frontendproyectoapp.viewModel.BuscarAlimentoViewModel
import com.example.frontendproyectoapp.viewModel.BuscarAlimentoViewModelFactory
import com.example.frontendproyectoapp.viewModel.UsuarioViewModel

@Composable
fun RegistroVent8Screen(navController: NavController, usuarioViewModel: UsuarioViewModel) {
    val context = LocalContext.current
    val viewModel: BuscarAlimentoViewModel = viewModel(
        factory = BuscarAlimentoViewModelFactory(context.applicationContext as Application)
    )

    LaunchedEffect(Unit) {
        viewModel.cargarAlimentosAgrupados()
    }

    RegistroVent8ScreenContent(
        buscarViewModel = viewModel,
        usuarioViewModel = usuarioViewModel,
        onBackClick = { navController.popBackStack() },
        onContinueClick = { navController.navigate("registro9") }
    )
}

@Composable
fun RegistroVent8ScreenContent(
    buscarViewModel: BuscarAlimentoViewModel,
    usuarioViewModel: UsuarioViewModel,
    onBackClick: () -> Unit = {},
    onContinueClick: () -> Unit = {}
) {
    val alimentosAgrupados = buscarViewModel.alimentosAgrupados
    val seleccionados = usuarioViewModel.alimentosFavoritos

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        IconButton(onClick = onBackClick) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Volver")
        }

        Text(
            text = "Selecciona tus alimentos más consumidos",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        if (alimentosAgrupados.isEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("No se encontraron alimentos para mostrar.", color = Color.Red)
        }

        alimentosAgrupados.forEach { (categoria, alimentos) ->
            CategoriaAlimentosComposable(
                titulo = categoria,
                alimentos = alimentos,
                seleccionados = seleccionados,
                onToggle = { usuarioViewModel.toggleFavorito(it) }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onContinueClick,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Continuar")
        }
    }
}

@Composable
fun CategoriaAlimentosComposable(
    titulo: String,
    alimentos: List<Alimento>,
    seleccionados: List<Alimento>,
    onToggle: (Alimento) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold
        )

        val filas = alimentos.chunked(3)
        filas.forEach { fila ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                fila.forEach { alimento ->
                    AlimentoItemUrl(
                        alimento = alimento,
                        esSeleccionado = seleccionados.any { it.idAlimento == alimento.idAlimento },
                        onToggle = { onToggle(alimento) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun AlimentoItemUrl(
    alimento: Alimento,
    esSeleccionado: Boolean,
    onToggle: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .clickable { onToggle() }
                .border(2.dp, if (esSeleccionado) Color.Green else Color.LightGray, CircleShape)
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = alimento.urlImagen ?: "https://via.placeholder.com/100"
                ),
                contentDescription = alimento.nombreAlimento,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = alimento.nombreAlimento,
            fontSize = 12.sp,
            fontWeight = if (esSeleccionado) FontWeight.Bold else FontWeight.Normal
        )
    }
}

/*
@Preview(showBackground = true)
@Composable
fun RegistroVent8ScreenPreview() {
    RegistroVent8ScreenContent()
}

 */
