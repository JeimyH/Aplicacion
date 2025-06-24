package com.example.frontendproyectoapp.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material3.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontendproyectoapp.viewModel.UsuarioViewModel

@Composable
fun RegistroVent4Screen(navController: NavController, viewModel: UsuarioViewModel) {
    RegistroVent4ScreenContent(
        viewModel = viewModel,
        onBackClick = { navController.popBackStack() },
        onClick = { navController.navigate("registro5") }
    )
}

@Composable
fun RegistroVent4ScreenContent(
    viewModel: UsuarioViewModel,
    onClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    var pesoObjetivo by remember { mutableStateOf(viewModel.peso.toInt()) }
    val (pesoMin, pesoMax) = calcularRangoPesoNormal(viewModel.altura)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Botón atrás en la esquina superior izquierda
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Atrás")
        }

        // Contenido centrado y desplazable
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 8.dp), // padding para que no quede pegado a los bordes
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Establece tu peso objetivo",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Según tu altura, tu peso saludable está entre $pesoMin kg y $pesoMax kg",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = pesoObjetivo.toString(),
                onValueChange = {
                    pesoObjetivo = it.toIntOrNull() ?: pesoObjetivo
                },
                label = { Text("Peso objetivo (kg)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }

        // Botón continuar en la parte inferior
        Button(
            onClick = {
                viewModel.pesoObjetivo = pesoObjetivo.toFloat()
                onClick()
            },
            enabled = pesoObjetivo in 30..300,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
                .fillMaxWidth(0.5f) // botón medio ancho, ajusta si quieres otro tamaño
        ) {
            Text("Continuar")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RegistroVent4ScreenPreview(viewModel: UsuarioViewModel = viewModel()) {
    RegistroVent4ScreenContent(viewModel = viewModel)
}

fun calcularRangoPesoNormal(alturaCm: Float): Pair<Int, Int> {
    val alturaM = alturaCm / 100f
    val pesoMin = 18.5 * (alturaM * alturaM)
    val pesoMax = 24.9 * (alturaM * alturaM)
    return Pair(pesoMin.toInt(), pesoMax.toInt())
}